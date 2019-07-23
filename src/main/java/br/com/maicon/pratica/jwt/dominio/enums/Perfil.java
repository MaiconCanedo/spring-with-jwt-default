package br.com.maicon.pratica.jwt.dominio.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Perfil {

    ADMIN,
    CLIENTE;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority("ROLE_".concat(this.name()));
    }
}