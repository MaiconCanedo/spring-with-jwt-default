package br.com.maicon.pratica.jwt.dominio.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
