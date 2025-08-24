package com.thiagosalvess.booking.application.services;

import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.infrastructure.repositories.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    private FakeUserRepository fakeUserRepository;

    @BeforeEach
    public void setUp() {
        fakeUserRepository = new FakeUserRepository();
        userService = new UserService(fakeUserRepository);
    }

    @Test
    public void shouldReturnNullWhenAnInvalidIdIsPassed() {
        var user = userService.findUserById(UUID.randomUUID());

        assertEquals(Optional.empty(), user);
    }

    @Test
    public void shouldReturnAnUserWhenAValidIdIsProvided() {
        var uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var user = userService.findUserById(uuid);

        assertNotNull(user);
        assertEquals(uuid, user.get().getId());
        assertEquals("João Ribeiro", user.get().getName());
    }

    @Test
    public void shouldSaveANewUserSuccessfulyUsingRepositoryFakeAndFindAgain() {
        var uuid = UUID.randomUUID();
        var newUser = new User(uuid, "Test user");
        userService.save(newUser);

        var user = userService.findUserById(uuid);
        assertNotNull(user);
        assertEquals(uuid, user.get().getId());
        assertEquals("Test user", user.get().getName());
    }

}