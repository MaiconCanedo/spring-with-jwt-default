package br.com.maicon.pratica.jwt.dominio.service;

import br.com.maicon.pratica.jwt.dominio.entity.Cliente;
import br.com.maicon.pratica.jwt.dominio.enums.Perfil;
import br.com.maicon.pratica.jwt.dominio.persistence.repository.ClienteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public boolean update(Long id, Map<String, Object> clienteMap) {
        final Optional<Cliente> clienteHipotetico = clienteRepository.findById(id);
        fixTypes(clienteMap);
        clienteHipotetico.ifPresent(cliente ->
                clienteMap.forEach((key, value) -> setItem(key, value, cliente)));
        clienteHipotetico.ifPresent(clienteRepository::save);
        return clienteHipotetico.isPresent();
    }

    private void setItem(String key, Object value, Cliente cliente) {
        final Class<Cliente> clienteClasse = Cliente.class;
        try {
            final Class<?> fieldType = clienteClasse.getDeclaredField(key).getType();
            clienteClasse.getMethod("set".concat(StringUtils.capitalize(key)), fieldType)
                    .invoke(cliente, fieldType.cast(value));
        } catch (NoSuchFieldException | NoSuchMethodException
                | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void fixTypes(Map<String, Object> clienteMap) {
        clienteMap.replaceAll((key, value) -> {
            if (value == null)
                return null;
            if (key.equals("perfis"))
                return new HashSet<>(((List<String>) value).stream()
                        .map(Perfil::valueOf)
                        .collect(Collectors.toList()));
            return value;
        });
    }
}
