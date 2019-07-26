package br.com.maicon.pratica.jwt.resource;

import br.com.maicon.pratica.jwt.security.AES256Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("encriptar")
public class EncriptarResource {

    private AES256Util aes256Util;

    public EncriptarResource() {
        aes256Util = new AES256Util("chaves");
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> encriptar(@RequestBody Map<String, String> objeto) throws Exception {
        final String texto_descriptografado = aes256Util.decrypt(objeto.get("texto_criptografado"));
        final HashMap<String, String> objetoPronto = new HashMap<>();
        objetoPronto.put("texto_descriptografado", texto_descriptografado);
        return ResponseEntity.ok(objetoPronto);
    }
}
