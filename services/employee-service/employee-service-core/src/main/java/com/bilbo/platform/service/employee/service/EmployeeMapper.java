package com.bilbo.platform.service.employee.service;

import com.bilbo.platform.service.employee.entity.EmployeeEntity;
import com.bilbo.platform.service.employee.model.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    EmployeeEntity mapToEntity(EmployeeDto dto);

    EmployeeDto mapToDto(EmployeeEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    EmployeeEntity updateEntity(@MappingTarget EmployeeEntity entity, EmployeeDto dto);
}
