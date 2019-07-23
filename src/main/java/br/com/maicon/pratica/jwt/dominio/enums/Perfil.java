package br.com.maicon.pratica.jwt.dominio.enums;

import java.util.Arrays;

import static org.springframework.util.StringUtils.hasLength;

public enum Perfil {

    ADMIN(0, "ROLE_ADMIN"),
    CLIENTE(1, "ROLE_CLIENTE");

    private Integer codigo;
    private String descricao;

    Perfil(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static Perfil get(Integer codigo) {
        if (codigo == null)
            return null;
        return Arrays.stream(Perfil.values())
                .filter(perfil -> perfil.codigo.equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public static Perfil get(String descricao) {
        if (hasLength(descricao))
            return null;
        return Arrays.stream(Perfil.values())
                .filter(perfil -> perfil.descricao.equals(descricao))
                .findFirst()
                .orElse(null);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}