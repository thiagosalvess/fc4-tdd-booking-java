package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.Booking;
import com.thiagosalvess.booking.domain.repositories.BookingRepository;
import com.thiagosalvess.booking.infrastructure.persistence.mappers.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryJpa implements BookingRepository {
    private final SpringDataBookingJpa jpa;
    private final BookingMapper mapper;

    @Override
    public void save(Booking booking) {
        jpa.save(mapper.toEntity(booking));
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }
}
