package com.thiagosalvess.booking.domain.entities;

import com.thiagosalvess.booking.domain.exception.GuestCountExceededException;
import com.thiagosalvess.booking.domain.exception.MaxGuestsMustBePositiveException;
import com.thiagosalvess.booking.domain.exception.PropertyNameRequiredException;
import com.thiagosalvess.booking.domain.valueobject.DateRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertyTest {

    @Test
    public void shouldCreateAnInstanceOfPropertyWithAllAttributes() {
        var uuid = UUID.randomUUID();
        var property = new Property(
                uuid,
                "Casa de praia",
                "Uma bela casa de praia",
                4,
                200
        );

        assertEquals(uuid, property.getId());
        assertEquals("Casa de praia", property.getName());
        assertEquals("Uma bela casa de praia", property.getDescription());
        assertEquals(4, property.getMaxGuests());
        assertEquals(200, property.getBasePricePerNight());
    }

    @Test
    public void shouldThrowErrorIfTheNameIsEMpty() throws Exception {
        PropertyNameRequiredException ex = assertThrows(PropertyNameRequiredException.class, () -> new Property(UUID.randomUUID(), "", "descrição", 4, 200));
        assertEquals("O nome é obrigatório!", ex.getMessage());
    }

    @Test
    public void shouldThrowErrorIfTheMaxGuestsItsZeroOrNegative() throws Exception {
        MaxGuestsMustBePositiveException ex = assertThrows(MaxGuestsMustBePositiveException.class, () -> new Property(UUID.randomUUID(), "Nome casa", "descrição", 0, 200));
        assertEquals("O número máximo de hóspedes deve ser maior que zero!", ex.getMessage());
    }

    @Test
    public void shouldValidateTheNumberMaxOfMaxGuests() throws Exception {
        var property = new Property(UUID.randomUUID(), "Nome casa", "descrição", 5, 200);
        GuestCountExceededException ex = assertThrows(GuestCountExceededException.class, () -> property.validateGuestCount(6));
        assertEquals("Número máximo de hóspedes excedido. Máximo permitido: 5.", ex.getMessage());
    }

    @Test
    public void shouldNotApplyDiscountToStaysOfLessThanSevenNights() {
        var property = new Property(UUID.randomUUID(), "Nome casa", "descrição", 5, 100);
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 27)
        );
        var totalPrice = property.calculateTotalPrice(dateRange);
        assertEquals(500, totalPrice);
    }

    @Test
    public void shouldApplyDiscountToStaysForMoreThenSevenNights() {
        var property = new Property(UUID.randomUUID(), "Nome casa", "descrição", 7, 100);
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 29)
        );
        var totalPrice = property.calculateTotalPrice(dateRange);
        assertEquals(630, totalPrice);
    }

    @Test
    public void shouldCheckPropertyAvailability() {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "Nome casa", "descrição", 7, 100);
        var guest = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 29)
        );
        var dateRange1 = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 30)
        );

        new Booking(uuid, property, guest, dateRange.getStartDate(), dateRange.getEndDate(), 2);
        assertEquals(false, property.isAvailable(dateRange));
        assertEquals(false, property.isAvailable(dateRange1));
    }
}