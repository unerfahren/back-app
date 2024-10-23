package com.bilbo.platform.service.client.service.impl;

import com.bilbo.platform.repository.SpecBuilder;
import com.bilbo.platform.service.client.entity.ClientEntity;
import com.bilbo.platform.service.client.model.ClientDto;
import com.bilbo.platform.service.client.model.ClientDtoFilter;
import com.bilbo.platform.service.client.repository.ClientRepository;
import com.bilbo.platform.service.client.service.ClientMapper;
import com.bilbo.platform.service.client.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    @Override
    @Transactional
    public ClientDto createClient(ClientDto clientDto) {
        final var client = mapper.mapToEntity(clientDto);
        final var saved = repository.save(client);
        return mapper.mapToDto(saved);
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ClientDto getClient(Long id) {
        return repository.findById(id)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("client with id %s not found".formatted(id)));
    }

    @Override
    public Page<ClientDto> getClients(ClientDtoFilter filter, Pageable pageable) {
        final var pageRequest = Optional.ofNullable(pageable)
                .orElseGet(this::createDefaultPageable);

        return Optional.ofNullable(filter)
                .flatMap(this::createSpec)
                .map(spec -> repository.findAll(spec, pageRequest))
                .orElseGet(() -> repository.findAll(pageRequest))
                .map(mapper::mapToDto);
    }

    private Optional<Specification<ClientEntity>> createSpec(ClientDtoFilter filter) {
        final var regDateRange = SpecBuilder.Range.of(filter.getRegDateAfter(), filter.getRegDateBefore());

        return new SpecBuilder<ClientEntity>()
                .with(SpecBuilder::stringIs, "email", filter.getEmail())
                .with(SpecBuilder::stringIs, "name", filter.getName())
                .with(SpecBuilder::stringIs, "lastName", filter.getLastName())
                .with(SpecBuilder::valueIs, "birthsDate", filter.getBirthsDate())
                .with(SpecBuilder::valueIs, "registrationDate", filter.getRegDate())
                .with(SpecBuilder::dateAfter, "registrationDate", filter.getRegDateAfter())
                .with(SpecBuilder::dateBefore, "registrationDate", filter.getRegDateBefore())
                .with(SpecBuilder::dateRange, "registrationDate", regDateRange)
                .build(Specification::and);
    }

    private Pageable createDefaultPageable() {
        final var sort = Sort.sort(ClientEntity.class)
                .by(ClientEntity::getEmail)
                .ascending();

        return PageRequest.of(0, 10, sort);
    }

    @Override
    @Transactional
    public ClientDto updateClient(Long id, ClientDto clientDto) {
        return repository.findClientEntityById(id)
                .map(entity -> mapper.updateClientEntity(entity, clientDto))
                .map(repository::save)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("client with id %s not found".formatted(id)));
    }
}
