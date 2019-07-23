package br.com.maicon.pratica.jwt.dominio.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
    public ObjectNotFoundException() {
        super();
    }
}
