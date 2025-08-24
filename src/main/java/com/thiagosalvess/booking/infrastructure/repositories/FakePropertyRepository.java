package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.domain.repositories.PropertyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakePropertyRepository implements PropertyRepository {
    private List<Property> properties = new ArrayList<>(
            List.of(new Property(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Casa de praia", "casa de praia", 4, 100),
                    new Property(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Apartamento no centro", "apartamento no centro",5, 200)
            )
    );

    @Override
    public void save(Property property) {
        this.properties.add(property);
    }

    @Override
    public Optional<Property> findById(UUID id) {
        return properties.stream()
                .filter(property -> property.getId().equals(id))
                .findFirst();

    }
}
