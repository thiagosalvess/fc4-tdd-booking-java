package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.domain.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakeUserRepository implements UserRepository {
    private List<User> users = new ArrayList<>(
            List.of(
                    new User(UUID.fromString("11111111-1111-1111-1111-111111111111"), "João Ribeiro"),
                    new User(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Maria")
            )
    );

    @Override
    public void save(User user) {
        this.users.add(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
}
