package com.bilbo.platform.service.employee.controller;

import com.bilbo.platform.service.employee.api.EmployeesApi;
import com.bilbo.platform.service.employee.model.EmployeeDto;
import com.bilbo.platform.service.employee.model.EmployeeDtoFilter;
import com.bilbo.platform.service.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeesApi {

    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<Void> createEmployee(EmployeeDto employeeDto) {
        final var employee = employeeService.createEmployee(employeeDto);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteEmployee(Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok()
                .build();
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployee(Long id) {
        final var employee = employeeService.getEmployee(id);
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<PagedModel<EmployeeDto>> getEmployees(EmployeeDtoFilter filter, Pageable pageable) {
        final var employees = employeeService.getEmployees(filter, pageable);
        return ResponseEntity.ok(new PagedModel<>(employees));
    }

    @Override
    public ResponseEntity<EmployeeDto> updateEmployee(Long id, EmployeeDto employeeDto) {
        final var employee = employeeService.updateEmployee(id, employeeDto);
        return ResponseEntity.ok(employee);
    }
}
