package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.domain.repositories.PropertyRepository;
import com.thiagosalvess.booking.infrastructure.persistence.mappers.PropertyMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PropertyRepositoryJpa implements PropertyRepository {

    private SpringDataPropertyJpa jpa;
    private PropertyMapper mapper;

    public PropertyRepositoryJpa(SpringDataPropertyJpa springDataPropertyJpa, PropertyMapper mapper) {
        this.jpa = springDataPropertyJpa;
        this.mapper = mapper;
    }

    @Override
    public void save(Property property) {
        jpa.save(mapper.toEntity(property));
    }

    @Override
    public Optional<Property> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }
}
