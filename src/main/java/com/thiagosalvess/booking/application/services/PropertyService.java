package com.thiagosalvess.booking.application.services;

import com.thiagosalvess.booking.application.dtos.CreatePropertyDto;
import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.domain.repositories.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PropertyService {
    private PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Optional<Property> findPropertyById(UUID id) {
        return propertyRepository.findById(id);
    }

    public void save(Property property) {
        this.propertyRepository.save(property);
    }

    public Property createProperty(CreatePropertyDto dto) {
        var property = new Property(UUID.randomUUID(), dto.name(), dto.description(), dto.maxGuests(), dto.basePricePerNight());

        this.propertyRepository.save(property);

        return property;
    }
}
