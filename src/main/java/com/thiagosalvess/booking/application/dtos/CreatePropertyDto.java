package com.thiagosalvess.booking.application.dtos;

public record CreatePropertyDto(String name,
                                String description,
                                int maxGuests,
                                double basePricePerNight) {
}
