package com.thiagosalvess.booking.application.services;

import com.thiagosalvess.booking.application.dtos.CreateUserDto;
import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.domain.exception.PropertyNameRequiredException;
import com.thiagosalvess.booking.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserById(UUID id) {
        return this.userRepository.findById(id);
    }

    public void save(User user) {
        this.userRepository.save(user);
    }

    public User createUser(CreateUserDto dto) {
        var user = new User(UUID.randomUUID(), dto.name());

        this.userRepository.save(user);
        return user;
    }
}
