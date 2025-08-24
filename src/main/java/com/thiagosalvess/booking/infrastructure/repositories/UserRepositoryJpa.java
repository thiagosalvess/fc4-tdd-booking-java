package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.domain.repositories.UserRepository;
import com.thiagosalvess.booking.infrastructure.persistence.mappers.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryJpa implements UserRepository {
    private final SpringDataUserJpa jpa;
    private final UserMapper mapper;

    public UserRepositoryJpa(SpringDataUserJpa jpa, UserMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public void save(User user) {
       jpa.save(mapper.toEntity(user));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }
}
