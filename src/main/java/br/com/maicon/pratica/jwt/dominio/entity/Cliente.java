package br.com.maicon.pratica.jwt.dominio.entity;

import br.com.maicon.pratica.jwt.dominio.enums.Perfil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "perfil", joinColumns = @JoinColumn(name = "fk_cliente"))
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "perfil")
    private Set<Perfil> perfis;

    public Cliente() {
    }

    public static Builder builder() {
        return Builder.create();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(Set<Perfil> perfis) {
        this.perfis = perfis;
    }

    public void addPerfil(Perfil... perfis) {
        if (this.perfis == null)
            this.perfis = new HashSet<>();
        this.perfis.addAll(Arrays.asList(perfis));
    }

    public static final class Builder {
        private Cliente cliente;

        private Builder() {
            cliente = new Cliente();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder id(Long id) {
            cliente.setId(id);
            return this;
        }

        public Builder nome(String nome) {
            cliente.setNome(nome);
            return this;
        }

        public Builder email(String email) {
            cliente.setEmail(email);
            return this;
        }

        public Builder password(String password) {
            cliente.setPassword(password);
            return this;
        }

        public Builder perfis(Set<Perfil> perfis) {
            cliente.setPerfis(perfis);
            return this;
        }

        public Builder addPerfil(Perfil perfil) {
            cliente.addPerfil(perfil);
            return this;
        }

        public Cliente build() {
            return cliente;
        }
    }
}
