package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.domain.repositories.PropertyRepository;
import com.thiagosalvess.booking.infrastructure.persistence.mappers.PropertyMapperImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import({PropertyRepositoryJpa.class, PropertyMapperImpl.class})
class PropertyRepositoryJpaTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private SpringDataPropertyJpa springDataPropertyJpa;


    @Test
    public void shouldSavePropertySuccessfuly() {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "Casa na praia", "vista para o mar", 6, 200);

        propertyRepository.save(property);

        var savedProperty = springDataPropertyJpa.findById(uuid);
        assertTrue(savedProperty.isPresent());
        assertEquals(uuid, savedProperty.get().getId());
        assertEquals("Casa na praia", savedProperty.get().getName());
        assertEquals("vista para o mar", savedProperty.get().getDescription());
        assertEquals(6, savedProperty.get().getMaxGuests());
        assertEquals(200, savedProperty.get().getBasePricePerNight());
    }

    @Test
    public void ShouldReturnPropertyWhenIdIsValid(){
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "Casa na praia", "vista para o mar", 6, 200);

        propertyRepository.save(property);

        var savedProperty = propertyRepository.findById(uuid);
        assertTrue(savedProperty.isPresent());
        assertEquals(uuid, savedProperty.get().getId());
        assertEquals("Casa na praia", savedProperty.get().getName());
        assertEquals("vista para o mar", savedProperty.get().getDescription());
        assertEquals(6, savedProperty.get().getMaxGuests());
        assertEquals(200, savedProperty.get().getBasePricePerNight());
    }

    @Test
    public void shouldReturnEmptyWhenSearchingForNonExistentUser() {
        assertTrue(propertyRepository.findById(UUID.randomUUID()).isEmpty());
    }
}