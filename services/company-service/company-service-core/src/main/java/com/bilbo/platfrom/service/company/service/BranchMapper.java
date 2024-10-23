package com.bilbo.platfrom.service.company.service;

import com.bilbo.platfrom.service.company.entity.BranchEntity;
import com.bilbo.platfrom.service.company.model.BranchDto;
import com.bilbo.platfrom.service.company.model.GeoLocationDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {

    GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "address.id", ignore = true)
    @Mapping(target = "address.branch", ignore = true)
    BranchEntity mapToEntity(BranchDto dto);

    BranchDto mapToDto(BranchEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address.id", ignore = true)
    @Mapping(target = "address.branch", ignore = true)
    @Mapping(target = "company", ignore = true)
    BranchEntity updateEntity(@MappingTarget BranchEntity entity, BranchDto dto);

    default Point locationDtoToLocationEntity(GeoLocationDto geoLocationDto) {
        return factory.createPoint(new Coordinate(geoLocationDto.getLongitude(), geoLocationDto.getLatitude()));
    }

    default GeoLocationDto locationEntityToLocationDto(org.locationtech.jts.geom.Point point) {
        return new GeoLocationDto(point.getX(), point.getY());
    }
}
