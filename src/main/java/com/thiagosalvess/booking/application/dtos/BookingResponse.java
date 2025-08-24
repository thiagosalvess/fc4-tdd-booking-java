package com.thiagosalvess.booking.application.dtos;

import com.thiagosalvess.booking.domain.entities.Booking;

import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(UUID id, UUID propertyId, UUID guestId, LocalDate startDate, LocalDate endDate,
                              int guestCount, double totalPrice, String status) {
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(booking.getId(), booking.getProperty().getId(), booking.getGuest().getId(),
                booking.getDateRange().getStartDate(), booking.getDateRange().getEndDate(), booking.getGuestCount(),
                booking.getTotalPrice(), booking.getStatus().name());
    }
}
