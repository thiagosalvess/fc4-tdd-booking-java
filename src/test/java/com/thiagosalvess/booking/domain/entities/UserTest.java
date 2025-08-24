package com.thiagosalvess.booking.domain.entities;

import com.thiagosalvess.booking.domain.exception.UserIdRequiredException;
import com.thiagosalvess.booking.domain.exception.UserNameRequiredException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    public void shouldCreateAnInstanceOfUserWithIdAndName() {
        var uuid = UUID.randomUUID();
        var user  = new User(uuid,"João Ribeiro");
        assertEquals(uuid, user.getId());
        assertEquals("João Ribeiro", user.getName());
    }

    @Test
    public void shouldThrowAnErrorIfNameIsEmpty() throws Exception {
        UserNameRequiredException ex = assertThrows(UserNameRequiredException.class, () -> new User(UUID.randomUUID(),""));
        assertEquals("O nome é obrigatório!", ex.getMessage());
    }

    @Test
    public void shouldThrowAnErrorIfIdIsEmpty() throws Exception {
        UserIdRequiredException ex = assertThrows(UserIdRequiredException.class, () -> new User(null,"João"));
        assertEquals("O ID é obrigatório!", ex.getMessage());
    }
}