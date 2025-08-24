package com.thiagosalvess.booking.domain.repositories;

import com.thiagosalvess.booking.domain.entities.Property;

import java.util.Optional;
import java.util.UUID;

public interface PropertyRepository {
    void save(Property property);

    Optional<Property> findById(UUID id);
}
