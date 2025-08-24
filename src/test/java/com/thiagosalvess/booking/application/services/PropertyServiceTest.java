package com.thiagosalvess.booking.application.services;

import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.infrastructure.repositories.FakePropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PropertyServiceTest {
    private PropertyService propertyService;
    private FakePropertyRepository fakePropertyRepository;

    @BeforeEach
    public void setUp() {
        fakePropertyRepository = new FakePropertyRepository();
        propertyService = new PropertyService(fakePropertyRepository);
    }

    @Test
    public void shouldReturnNullWhenAnInvalidIdIsPassed() {
        var property = propertyService.findPropertyById(UUID.randomUUID());
        assertEquals(Optional.empty(), property);
    }

    @Test
    public void shouldReturnAnUserWhenAValidIdIsProvided() {
        var uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var property = propertyService.findPropertyById(uuid);

        assertNotNull(property);
        assertEquals(uuid, property.get().getId());
        assertEquals("Casa de praia", property.get().getName());
    }

    @Test
    public void shouldSaveANewUserSuccessfulyUsingRepositoryFakeAndFindAgain() {
        var uuid = UUID.randomUUID();
        var newProperty = new Property(uuid, "Test property", "Test description", 4, 150);
        propertyService.save(newProperty);

        var user = propertyService.findPropertyById(uuid);
        assertNotNull(user);
        assertEquals(uuid, user.get().getId());
        assertEquals("Test property", user.get().getName());
    }
}