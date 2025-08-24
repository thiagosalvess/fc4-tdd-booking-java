package com.thiagosalvess.booking.application.dtos;

import com.thiagosalvess.booking.domain.entities.Property;

public record PropertyResponse(java.util.UUID id, String name, String description, int maxGuests,
                               double basePricePerNight) {
    public static PropertyResponse from(Property property) {
        return new PropertyResponse(property.getId(), property.getName(), property.getDescription(),
                property.getMaxGuests(), property.getBasePricePerNight());
    }
}
