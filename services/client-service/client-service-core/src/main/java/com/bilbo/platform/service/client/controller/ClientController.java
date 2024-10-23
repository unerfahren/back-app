package com.bilbo.platform.service.client.controller;

import com.bilbo.platform.service.client.api.ClientsApi;
import com.bilbo.platform.service.client.model.ClientDto;
import com.bilbo.platform.service.client.model.ClientDtoFilter;
import com.bilbo.platform.service.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClientController implements ClientsApi {

    private final ClientService clientService;

    @Override
    public ResponseEntity<Void> createClient(ClientDto clientDto) {
        log.info("Received request for creating client: {}", clientDto);
        final var client = clientService.createClient(clientDto);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();
        log.info("Client created, location: {}", location);

        return ResponseEntity.created(location)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteClient(Long id) {
        log.info("Received request for deleting client with id: {}", id);
        clientService.deleteClient(id);
        log.info("Client deleted");

        return ResponseEntity.ok()
                .build();
    }

    @Override
    public ResponseEntity<ClientDto> getClient(Long id) {
        final ClientDto client = clientService.getClient(id);
        return ResponseEntity.ok(client);
    }

    @Override
    public ResponseEntity<PagedModel<ClientDto>> getClients(ClientDtoFilter filter, Pageable pageable) {
        final var clients = clientService.getClients(filter, pageable);
        return ResponseEntity.ok(new PagedModel<>(clients));
    }

    @Override
    public ResponseEntity<ClientDto> updateClient(Long id, ClientDto clientDto) {
        log.info("Received request for update client with id: {}, client: {}", id, clientDto);
        final ClientDto client;
        client = clientService.updateClient(id, clientDto);
        log.info("Client was updated");
        return ResponseEntity.ok(client);
    }
}
