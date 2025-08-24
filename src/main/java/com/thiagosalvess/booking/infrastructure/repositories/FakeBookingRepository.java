package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.Booking;
import com.thiagosalvess.booking.domain.repositories.BookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakeBookingRepository implements BookingRepository {
   private List<Booking> bookings = new ArrayList<>();

    @Override
    public void save(Booking booking) {
        this.bookings.add(booking);
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return this.bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst();
    }
}
