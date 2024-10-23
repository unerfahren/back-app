package com.bilbo.platform.service.client.service;

import com.bilbo.platform.service.client.entity.ClientEntity;
import com.bilbo.platform.service.client.model.ClientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    ClientEntity mapToEntity(ClientDto dto);

    ClientDto mapToDto(ClientEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    ClientEntity updateClientEntity(@MappingTarget ClientEntity entity, ClientDto dto);
}
