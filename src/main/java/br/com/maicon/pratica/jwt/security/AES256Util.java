package br.com.maicon.pratica.jwt.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class AES256Util {

    private final static AES256Util AES_256_UTIL = new AES256Util("chaves");
    private String secret;

    public AES256Util(String secret) {
        this.secret = secret;
    }

    public static void main(String[] args) throws Exception {
        String token = "U2FsdGVkX1/ANTiEtcFawN+89l3cekm7f9aW5605u33mhsiNMqIH3Mxh+JCt8l23";
        final String texto = AES_256_UTIL.decrypt(token);
        System.out.println(token);
        System.out.println(texto);
//        final String senha = AES_256_UTIL.encrypt("Maicon");
//        System.out.println(senha);
//        System.out.println(new String(Base64.getDecoder().decode(senha), UTF_8));
    }

    /**
     * Generates a key and an initialization vector (IV) with the given salt and password.
     * <p>
     * This method is equivalent to OpenSSL's EVP_BytesToKey function
     * (see https://github.com/openssl/openssl/blob/master/crypto/evp/evp_key.c).
     * By default, OpenSSL uses a single iteration, MD5 as the algorithm and UTF-8 encoded password data.
     * </p>
     * <p>
     * <p>
     *
     * @param keyLength  the length of the generated key (in bytes)
     * @param ivLength   the length of the generated IV (in bytes)
     * @param iterations the number of digestion rounds
     * @param salt       the salt data (8 bytes of data or <code>null</code>)
     * @param password   the password data (optional)
     * @param md         the message digest algorithm to use
     * @return an two-element array with the generated key and IV
     */
    public byte[][] generateKeyAndIV(int keyLength, int ivLength, int iterations,
                                     byte[] salt, byte[] password, MessageDigest md) {
        int digestLength = md.getDigestLength();
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        byte[] generatedData = new byte[requiredLength];
        int generatedLength = 0;

        try {
            md.reset();

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0)
                    md.update(generatedData, generatedLength - digestLength, digestLength);
                md.update(password);
                if (salt != null)
                    md.update(salt, 0, 8);
                md.digest(generatedData, generatedLength, digestLength);

                // additional rounds
                for (int i = 1; i < iterations; i++) {
                    md.update(generatedData, generatedLength, digestLength);
                    md.digest(generatedData, generatedLength, digestLength);
                }

                generatedLength += digestLength;
            }

            // Copy key and IV into separate byte arrays
            byte[][] result = new byte[2][];
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
            if (ivLength > 0)
                result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

            return result;

        } catch (DigestException e) {
            throw new RuntimeException(e);

        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, (byte) 0);
        }
    }

    public String encrypt(String text) throws Exception {

        final byte[] cipherData = Base64.getDecoder()
                .decode("U2FsdGVkX1+VfltxXx9ObwqyOv7mQKIRh1oDvXJdzqA=");
        final byte[][] keyAndIV = generateKeyAndIV(
                32, 16, 1,
                Arrays.copyOfRange(cipherData, 8, 16),
                secret.getBytes(UTF_8),
                MessageDigest.getInstance("MD5"));
        byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCBC.init(ENCRYPT_MODE,
                new SecretKeySpec(keyAndIV[0], "AES"),
                new IvParameterSpec(keyAndIV[1]));
        return Base64.getEncoder()
                .encodeToString(aesCBC.doFinal(text.getBytes(UTF_8)));
    }

    public String decrypt(String text) throws Exception {
        final byte[] cipherData = Base64.getDecoder().decode(text);
        final byte[][] keyAndIV = generateKeyAndIV(
                32, 16, 1,
                Arrays.copyOfRange(cipherData, 8, 16),
                secret.getBytes(UTF_8),
                MessageDigest.getInstance("MD5"));
        return getText(cipherData, keyAndIV);
    }

    private String getText(byte[] cipherData, byte[][] keyAndIV) throws Exception {
        byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCBC.init(DECRYPT_MODE,
                new SecretKeySpec(keyAndIV[0], "AES"),
                new IvParameterSpec(keyAndIV[1]));
        return new String(aesCBC.doFinal(encrypted), UTF_8);
    }
}