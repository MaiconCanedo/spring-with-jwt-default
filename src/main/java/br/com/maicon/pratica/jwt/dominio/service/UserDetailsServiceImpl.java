package br.com.maicon.pratica.jwt.dominio.service;

import br.com.maicon.pratica.jwt.dominio.persistence.repository.ClienteRepository;
import br.com.maicon.pratica.jwt.security.UserSpringSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    public UserDetailsServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clienteRepository.findByEmail(email)
                .map(UserSpringSecurity::new)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
