package br.com.maicon.pratica.jwt.dominio.service;

import br.com.maicon.pratica.jwt.dominio.entity.Cliente;
import br.com.maicon.pratica.jwt.dominio.persistence.repository.ClienteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder bCrypt;

    public ClienteService(ClienteRepository clienteRepository, BCryptPasswordEncoder bCrypt) {
        this.clienteRepository = clienteRepository;
        this.bCrypt = bCrypt;
    }

    public Optional<Cliente> findByEmailAndPassword(String user, String password) {
        return clienteRepository.findByEmailAndPassword(user, password);
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Cliente save(Cliente cliente) {
        cliente.setPassword(bCrypt.encode(cliente.getPassword()));
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
}
