package br.com.maicon.pratica.jwt.security;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class AesUtil {
    private final int keySize;
    private final int iterationCount;
    private final Cipher cipher;

    public AesUtil(int keySize, int iterationCount) {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
    }

    public String encrypt(String salt, String iv, String passphrase, String plaintext) {
        SecretKey key = generateKey(salt, passphrase);
        byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes(StandardCharsets.UTF_8));
        return base64(encrypted);
    }

    public String decrypt(String salt, String iv, String passphrase, String ciphertext) {
        SecretKey key = generateKey(salt, passphrase);
        byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        }
        catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            throw fail(e);
        }
    }

    private SecretKey generateKey(String salt, String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw fail(e);
        }
    }

    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }

    public static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    public static String hex(byte[] bytes) {
        return HexBin.encode(bytes);
    }

    public static byte[] hex(String str) {
        try {
            return HexBin.decode(String.valueOf(str.toCharArray()));
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }

    public static void main(String[] args) {
        final AesUtil aesUtil = new AesUtil(32, 1);
        System.out.println(aesUtil.encrypt("", "��W�6��ư�w}", "chaves", "Maicon"));
    }
}