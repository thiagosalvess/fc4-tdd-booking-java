package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.infrastructure.persistence.entities.PropertyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    @Mapping(target = "bookings", ignore = true)
    PropertyEntity toEntity(Property domain);


    @Mapping(target = "bookings", ignore = true)
    Property toDomain(PropertyEntity entity);
}
