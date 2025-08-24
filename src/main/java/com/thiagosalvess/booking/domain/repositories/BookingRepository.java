package com.thiagosalvess.booking.domain.repositories;

import com.thiagosalvess.booking.domain.entities.Booking;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
    void save(Booking booking);
    Optional<Booking> findById(UUID id);
}
