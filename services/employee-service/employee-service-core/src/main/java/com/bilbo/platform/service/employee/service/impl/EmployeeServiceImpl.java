package com.bilbo.platform.service.employee.service.impl;

import com.bilbo.platform.repository.SpecBuilder;
import com.bilbo.platform.service.employee.entity.EmployeeEntity;
import com.bilbo.platform.service.employee.model.EmployeeDto;
import com.bilbo.platform.service.employee.model.EmployeeDtoFilter;
import com.bilbo.platform.service.employee.repository.EmployeeRepository;
import com.bilbo.platform.service.employee.service.EmployeeMapper;
import com.bilbo.platform.service.employee.service.EmployeeService;
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
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    @Override
    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        final var employee = mapper.mapToEntity(employeeDto);
        final var saved = repository.save(employee);
        return mapper.mapToDto(saved);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }

    @Override
    public EmployeeDto getEmployee(Long id) {
        return repository.findById(id)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("employee with id %s not found".formatted(id)));
    }

    @Override
    public Page<EmployeeDto> getEmployees(EmployeeDtoFilter filter, Pageable pageable) {
        final var pageRequest = Optional.ofNullable(pageable)
                .orElseGet(this::createDefaultPageable);

        return Optional.ofNullable(filter)
                .flatMap(this::createSpec)
                .map(spec -> repository.findAll(spec, pageRequest))
                .orElseGet(() -> repository.findAll(pageRequest))
                .map(mapper::mapToDto);
    }

    private Optional<Specification<EmployeeEntity>> createSpec(EmployeeDtoFilter filter) {
        final var regDateRange = SpecBuilder.Range.of(filter.getRegDateAfter(), filter.getRegDateBefore());

        return new SpecBuilder<EmployeeEntity>()
                .with(SpecBuilder::stringIs, "email", filter.getEmail())
                .with(SpecBuilder::stringIs, "name", filter.getName())
                .with(SpecBuilder::stringIs, "lastName", filter.getLastName())
                .with(SpecBuilder::valueIs, "birthsDate", filter.getBirthsDate())
                .with(SpecBuilder::valueIs, "registrationDate", filter.getRegDate())
                .with(SpecBuilder::dateAfter, "registrationDate", filter.getRegDateAfter())
                .with(SpecBuilder::dateBefore, "registrationDate", filter.getRegDateBefore())
                .with(SpecBuilder::dateRange, "registrationDate", regDateRange)
                .with(SpecBuilder::valueIs, "companyId", filter.getCompanyId())
                .with(SpecBuilder::valueIs, "branchId", filter.getBranchId())
                .build(Specification::and);
    }

    private Pageable createDefaultPageable() {
        final var sort = Sort.sort(EmployeeEntity.class)
                .by(EmployeeEntity::getEmail)
                .ascending();

        return PageRequest.of(0, 10, sort);
    }

    @Override
    @Transactional
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        return repository.findById(id)
                .map(entity -> mapper.updateEntity(entity, employeeDto))
                .map(repository::save)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("employee with id %s not found".formatted(id)));
    }
}
