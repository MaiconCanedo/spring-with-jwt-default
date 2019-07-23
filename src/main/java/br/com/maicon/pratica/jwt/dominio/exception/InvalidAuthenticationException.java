package br.com.maicon.pratica.jwt.dominio.exception;

public class InvalidAuthenticationException extends RuntimeException {
    public InvalidAuthenticationException() {
        super("Autorização invalida.");
    }
}
