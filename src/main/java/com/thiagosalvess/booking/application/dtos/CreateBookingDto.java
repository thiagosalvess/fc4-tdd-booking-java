package com.thiagosalvess.booking.application.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record CreateBookingDto(UUID propertyId,
                               UUID guestId,
                               LocalDate startDate,
                               LocalDate endDate,
                               int guestCount) {
}
