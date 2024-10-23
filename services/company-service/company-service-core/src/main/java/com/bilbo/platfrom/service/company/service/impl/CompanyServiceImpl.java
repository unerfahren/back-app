package com.bilbo.platfrom.service.company.service.impl;

import com.bilbo.platform.repository.SpecBuilder;
import com.bilbo.platfrom.service.company.entity.CompanyEntity;
import com.bilbo.platfrom.service.company.model.CompanyDto;
import com.bilbo.platfrom.service.company.model.CompanyDtoFilter;
import com.bilbo.platfrom.service.company.repository.CompanyRepository;
import com.bilbo.platfrom.service.company.service.CompanyMapper;
import com.bilbo.platfrom.service.company.service.CompanyService;
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
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repository;
    private final CompanyMapper mapper;

    @Override
    @Transactional
    public CompanyDto createCompany(CompanyDto companyDto) {
        final var company = mapper.mapToEntity(companyDto);
        final var saved = repository.save(company);
        return mapper.mapToDto(saved);
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CompanyDto getCompany(Long id) {
        return repository.findById(id)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("client with id %s not found".formatted(id)));
    }

    @Override
    public Page<CompanyDto> getCompanies(CompanyDtoFilter filter, Pageable pageable) {
        final var pageRequest = Optional.ofNullable(pageable)
                .orElseGet(this::createDefaultPageable);

        return Optional.ofNullable(filter)
                .flatMap(this::createSpec)
                .map(spec -> repository.findAll(spec, pageRequest))
                .orElseGet(() -> repository.findAll(pageRequest))
                .map(mapper::mapToDto);
    }

    private Optional<Specification<CompanyEntity>> createSpec(CompanyDtoFilter filter) {
        return new SpecBuilder<CompanyEntity>()
                .with(SpecBuilder::stringIs, "name", filter.getName())
                .with(SpecBuilder::stringIs, "ownerEmail", filter.getOwnerEmail())
                .build(Specification::and);
    }

    private Pageable createDefaultPageable() {
        final var sort = Sort.sort(CompanyEntity.class)
                .by(CompanyEntity::getName)
                .ascending();

        return PageRequest.of(0, 10, sort);
    }

    @Override
    @Transactional
    public CompanyDto updateCompany(Long id, CompanyDto clientDto) {
        return repository.findLockedById(id)
                .map(entity -> mapper.updateEntity(entity, clientDto))
                .map(repository::save)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("client with id %s not found".formatted(id)));
    }
}
