package com.bilbo.platfrom.service.company.service;

import com.bilbo.platfrom.service.company.entity.CompanyEntity;
import com.bilbo.platfrom.service.company.model.CompanyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "branches", ignore = true)
    CompanyEntity mapToEntity(CompanyDto dto);

    CompanyDto mapToDto(CompanyEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "branches", ignore = true)
    CompanyEntity updateEntity(@MappingTarget CompanyEntity entity, CompanyDto dto);
}
