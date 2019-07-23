package br.com.maicon.pratica.jwt.security;

import br.com.maicon.pratica.jwt.dominio.entity.Cliente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserSpringSecurity implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSpringSecurity() {
    }

    public UserSpringSecurity(Cliente cliente) {
        this.id = cliente.getId();
        this.email = cliente.getEmail();
        this.password = cliente.getPassword();
        this.authorities = cliente.getPerfis().stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.authority()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
