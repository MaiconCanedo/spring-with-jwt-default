package br.com.maicon.pratica.jwt.dominio.persistence.repository;

import br.com.maicon.pratica.jwt.dominio.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmailAndPassword(String email, String password);

    Optional<Cliente> findByEmail(String email);

    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
