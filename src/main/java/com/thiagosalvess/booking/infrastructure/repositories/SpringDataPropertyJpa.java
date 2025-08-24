package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.infrastructure.persistence.entities.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPropertyJpa extends JpaRepository<PropertyEntity, UUID> {
}
