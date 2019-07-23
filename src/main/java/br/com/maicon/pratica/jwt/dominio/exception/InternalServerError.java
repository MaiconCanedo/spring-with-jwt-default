package br.com.maicon.pratica.jwt.dominio.exception;

public class InternalServerError extends RuntimeException {
    public InternalServerError(String message) {
        super(message);
    }
}
