package com.bilbo.platform.service.employee.service;

import com.bilbo.platform.service.employee.model.EmployeeDto;
import com.bilbo.platform.service.employee.model.EmployeeDtoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    void deleteEmployee(Long id);

    EmployeeDto getEmployee(Long id);

    Page<EmployeeDto> getEmployees(EmployeeDtoFilter filter, Pageable pageable);

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);
}
