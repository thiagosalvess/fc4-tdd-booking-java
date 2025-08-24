package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.domain.repositories.UserRepository;
import com.thiagosalvess.booking.infrastructure.persistence.mappers.UserMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import({UserRepositoryJpa.class, UserMapperImpl.class})
class UserRepositoryJpaTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.6");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpringDataUserJpa springDataUserJpa;

    @Test
    public void shouldSaveUserSuccessfuly() {
        var uuid = UUID.randomUUID();
        var user = new User(uuid, "John Doe");
        userRepository.save(user);

        var savedUser = springDataUserJpa.findById(uuid);
        assertTrue(savedUser.isPresent());
        assertEquals(uuid, savedUser.get().getId());
        assertEquals("John Doe", savedUser.get().getName());
    }

    @Test
    public void shouldReturnUserWhenIdIsValid() {
        var uuid = UUID.randomUUID();
        var user = new User(uuid, "John Doe");

        userRepository.save(user);

        var savedUser = userRepository.findById(uuid);
        assertTrue(savedUser.isPresent());
        assertEquals("John Doe", savedUser.get().getName());
    }

    @Test
    public void shouldReturnEmptyWhenSearchingForNonExistentUser() {
        assertTrue(userRepository.findById(UUID.randomUUID()).isEmpty());
    }

}