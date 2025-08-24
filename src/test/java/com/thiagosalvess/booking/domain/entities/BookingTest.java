package com.thiagosalvess.booking.domain.entities;

import com.thiagosalvess.booking.domain.exception.BookingAlreadyCanceledException;
import com.thiagosalvess.booking.domain.exception.GuestCountExceededException;
import com.thiagosalvess.booking.domain.exception.InvalidGuestCountException;
import com.thiagosalvess.booking.domain.exception.PropertyUnavailableException;
import com.thiagosalvess.booking.domain.valueobject.DateRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingTest {

    @Test
    public void shouldCreateAnInstanceOfBookingWithAllAttributes() {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "casa", "descrição", 4, 100);
        var user = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 27)
        );

        var booking = new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 2);
        assertEquals(uuid, booking.getId());
        assertEquals(property, booking.getProperty());
        assertEquals(user, booking.getGuest());
        assertEquals(dateRange.getStartDate(), booking.getDateRange().getStartDate());
        assertEquals(2, booking.getGuestCount());
    }

    @Test
    public void shouldThrowAnErrorIfTheNumberOfGuestsIsZeroOrNegative() throws Exception {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "casa", "descrição", 4, 100);
        var user = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 27)
        );

        InvalidGuestCountException ex = assertThrows(InvalidGuestCountException.class, () -> new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 0));
        assertEquals("O número de hóspedes deve ser maior que zero!", ex.getMessage());
    }

    @Test
    public void shouldThrowAnErrorWhenTryingToMakeAReservationWithNumberOfGuestsBiggerThenTheAllowed() throws Exception {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "casa", "descrição", 4, 100);
        var user = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 27)
        );

        GuestCountExceededException ex = assertThrows(GuestCountExceededException.class, () -> new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 5));
        assertEquals("Número máximo de hóspedes excedido. Máximo permitido: 4.", ex.getMessage());
    }

    @Test
    public void shouldCalculateTheTotalPriceWithDiscount() {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "casa", "descrição", 4, 100);
        var user = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 29)
        );

        var booking = new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 4);

        assertEquals(700 * 0.9, booking.getTotalPrice());
    }

    @Test
    public void shouldNotRealizeBookingWhenThePropertyIsNotAvailable() throws Exception {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "casa", "descrição", 4, 100);
        var user = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 29)
        );
        new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 4);
        var dateRange2 = new DateRange(
                LocalDate.of(2025, 8, 23),
                LocalDate.of(2025, 8, 28)
        );

        PropertyUnavailableException ex = assertThrows(PropertyUnavailableException.class, () -> new Booking(UUID.randomUUID(), property, user, dateRange2.getStartDate(), dateRange2.getEndDate(), 4));
        assertEquals("A propriedade não está disponível para o período selecionado!", ex.getMessage());
    }

    @Test
    public void shouldCancelAReservationWithoutRefundWhenThereIsLessThenADayToCheckinIn() {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "casa", "descrição", 4, 100);
        var user = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 20),
                LocalDate.of(2025, 8, 22)
        );
        var booking = new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 4);
        var currentDate = LocalDate.of(2025, 8, 20);
        booking.cancel(currentDate);

        assertEquals(Status.CANCELLED, booking.getStatus());
        assertEquals(200, booking.getTotalPrice());
    }

    @Test
    public void shouldCancelAReservationWithParcialRefundWhenIsAtOneToSevenDaysBeforeTheCheckIn() {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "casa", "descrição", 4, 100);
        var user = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 20),
                LocalDate.of(2025, 8, 25)
        );
        var booking = new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 4);
        var currentDate = LocalDate.of(2025, 8, 15);
        booking.cancel(currentDate);

        assertEquals(Status.CANCELLED, booking.getStatus());
        assertEquals(100 * 5 * 0.5, booking.getTotalPrice());
    }

    @Test
    public void shouldNotBePossibleToCancelTheSameReservationTwice() throws Exception {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "casa", "descrição", 4, 100);
        var user = new User(uuid, "João Ribeiro");
        var dateRange = new DateRange(
                LocalDate.of(2025, 8, 20),
                LocalDate.of(2025, 8, 25)
        );
        var booking = new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 4);
        var currentDate = LocalDate.of(2025, 8, 15);

        booking.cancel(currentDate);

        BookingAlreadyCanceledException ex = assertThrows(BookingAlreadyCanceledException.class, () -> booking.cancel(currentDate));
        assertEquals("A reserva já está cancelada!", ex.getMessage());
    }
}