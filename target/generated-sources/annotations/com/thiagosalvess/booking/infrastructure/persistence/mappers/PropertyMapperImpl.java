package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.infrastructure.persistence.entities.PropertyEntity;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T00:34:03+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class PropertyMapperImpl implements PropertyMapper {

    @Override
    public PropertyEntity toEntity(Property domain) {
        if ( domain == null ) {
            return null;
        }

        PropertyEntity propertyEntity = new PropertyEntity();

        propertyEntity.setId( domain.getId() );
        propertyEntity.setName( domain.getName() );
        propertyEntity.setDescription( domain.getDescription() );
        propertyEntity.setMaxGuests( domain.getMaxGuests() );
        propertyEntity.setBasePricePerNight( domain.getBasePricePerNight() );

        return propertyEntity;
    }

    @Override
    public Property toDomain(PropertyEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        int maxGuests = 0;
        double basePricePerNight = 0.0d;

        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        maxGuests = entity.getMaxGuests();
        basePricePerNight = entity.getBasePricePerNight();

        Property property = new Property( id, name, description, maxGuests, basePricePerNight );

        return property;
    }
}
