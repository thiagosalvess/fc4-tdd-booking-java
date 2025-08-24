package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.domain.exception.PropertyNameRequiredException;
import com.thiagosalvess.booking.infrastructure.persistence.entities.PropertyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PropertyMapperTest {
    private PropertyMapperImpl mapper;

    @BeforeEach
    public void setUp() {
        mapper = new PropertyMapperImpl();
    }

    @Test
    public void shouldConvertPropertyEntityToPropertyCorrectly() {
        UUID id = UUID.randomUUID();
        PropertyEntity entity = createPropertyEntity(id);

        Property domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Casa", domain.getName());
        assertEquals("Frente mar", domain.getDescription());
        assertEquals(6, domain.getMaxGuests());
        assertEquals(200, domain.getBasePricePerNight());
    }

    @Test
    public void shouldThrowValidationErrorWhenMissingRequiredFieldsInPropertyEntity() throws Exception{
        UUID id = UUID.randomUUID();
        PropertyEntity entity = createPropertyEntity(id);
        entity.setName("");

        assertThrows(PropertyNameRequiredException.class, () -> mapper.toDomain(entity));
    }

    @Test
    public void shouldConvertPropertyToPropertyEntityCorrectly() {
        UUID id = UUID.randomUUID();
        Property domain = new Property(id, "Chalé", "Serra", 4, 150);

        PropertyEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Chalé", entity.getName());
        assertEquals("Serra", entity.getDescription());
        assertEquals(4, entity.getMaxGuests());
        assertEquals(150, entity.getBasePricePerNight());
    }

    private PropertyEntity createPropertyEntity(UUID id) {
        PropertyEntity entity = new PropertyEntity();
        entity.setId(id);
        entity.setName("Casa");
        entity.setDescription("Frente mar");
        entity.setMaxGuests(6);
        entity.setBasePricePerNight(200);
        return entity;
    }
}