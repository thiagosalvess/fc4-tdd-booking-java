package com.thiagosalvess.booking.domain.repositories;

import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.infrastructure.persistence.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(UUID id);
}
