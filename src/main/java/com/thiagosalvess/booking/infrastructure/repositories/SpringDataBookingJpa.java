package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.infrastructure.persistence.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataBookingJpa extends JpaRepository<BookingEntity, UUID> {
}
