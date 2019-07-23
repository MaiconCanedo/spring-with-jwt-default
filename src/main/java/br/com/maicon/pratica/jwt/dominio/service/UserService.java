package br.com.maicon.pratica.jwt.dominio.service;

import br.com.maicon.pratica.jwt.security.UserSpringSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UserService {

    public static Optional<UserSpringSecurity> getAutheticated() {
        try {
            return Optional.ofNullable((UserSpringSecurity) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
