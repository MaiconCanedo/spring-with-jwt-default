package br.com.maicon.pratica.jwt.resource;

import br.com.maicon.pratica.jwt.dominio.dto.ClienteDTO;
import br.com.maicon.pratica.jwt.dominio.entity.Cliente;
import br.com.maicon.pratica.jwt.dominio.exception.NotFoundException;
import br.com.maicon.pratica.jwt.dominio.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("clientes")
public class ClienteResource {

    private final ClienteService clienteService;

    public ClienteResource(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        return ResponseEntity.ok(clienteService.findAll().stream()
                .map(ClienteDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ClienteDTO> find(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.findById(id)
                .map(ClienteDTO::new)
                .orElseThrow(() -> new NotFoundException("Cliente não entrado!")));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Cliente cliente) {
        final Cliente save = clienteService.save(cliente);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(save.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping(path = "{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody Map<String, Object> clienteMap) {
        if (clienteService.update(id, clienteMap))
            throw new NotFoundException("Cliente não encontrado");
        return ResponseEntity.ok().build();
    }
}
