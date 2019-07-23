package br.com.maicon.pratica.jwt.dominio.enums;

public enum Perfil {

    ADMIN,
    CLIENTE;

    public String authority() {
        return "ROLE_".concat(this.name());
    }
}