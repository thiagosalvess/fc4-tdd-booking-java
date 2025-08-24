package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataUserJpa extends JpaRepository<UserEntity, UUID> {
}
