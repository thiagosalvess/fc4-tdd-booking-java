package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.domain.exception.UserNameRequiredException;
import com.thiagosalvess.booking.infrastructure.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private UserMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new UserMapperImpl();
    }

    @Test
    public void shouldConvertUserEntityToUserCorrectly() {
        UUID id = UUID.randomUUID();
        UserEntity entity = createUserEntity(id);

        User domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("João", domain.getName());
    }

    @Test
    public void shouldThrowValidationErrorWhenMissingRequiredFieldsInPropertyEntity() throws Exception{
        UUID id = UUID.randomUUID();
        UserEntity entity = createUserEntity(id);
        entity.setName("");

        assertThrows(UserNameRequiredException.class, () -> mapper.toDomain(entity));
    }

    @Test
    public void shouldConvertPropertyToPropertyEntityCorrectly() {
        UUID id = UUID.randomUUID();
        User domain = new User(id, "João");

        UserEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("João", entity.getName());
    }

    private UserEntity createUserEntity(UUID id) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName("João");
        return entity;
    }
}