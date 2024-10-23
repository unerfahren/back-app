package com.bilbo.platform.service.client.service;

import com.bilbo.platform.service.client.model.ClientDto;
import com.bilbo.platform.service.client.model.ClientDtoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    ClientDto createClient(ClientDto clientDto);

    void deleteClient(Long id);

    ClientDto getClient(Long id);

    Page<ClientDto> getClients(ClientDtoFilter filter, Pageable pageable);

    ClientDto updateClient(Long id, ClientDto clientDto);
}
