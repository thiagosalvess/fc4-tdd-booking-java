package com.thiagosalvess.booking.infrastructure.repositories;

import com.thiagosalvess.booking.domain.entities.Booking;
import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.domain.entities.Status;
import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.domain.repositories.BookingRepository;
import com.thiagosalvess.booking.domain.repositories.PropertyRepository;
import com.thiagosalvess.booking.domain.repositories.UserRepository;
import com.thiagosalvess.booking.domain.valueobject.DateRange;
import com.thiagosalvess.booking.infrastructure.persistence.mappers.BookingMapperImpl;
import com.thiagosalvess.booking.infrastructure.persistence.mappers.PropertyMapperImpl;
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

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import({
        BookingRepositoryJpa.class, BookingMapperImpl.class,
        PropertyRepositoryJpa.class, PropertyMapperImpl.class,
        UserRepositoryJpa.class, UserMapperImpl.class
})
class BookingRepositoryJpaTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SpringDataBookingJpa springDataBookingJpa;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveBookingSuccessfuly(){
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "Casa na praia", "vista para o mar", 6, 200);
        propertyRepository.save(property);

        var user = new User(uuid, "João Ribeiro");
        userRepository.save(user);

        var dateRange = new DateRange(
                LocalDate.of(2025, 12, 20),
                LocalDate.of(2025, 12, 25)
        );

        var booking = new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 4);
        bookingRepository.save(booking);

        var saved = bookingRepository.findById(uuid);
        assertTrue(saved.isPresent());

        assertEquals(uuid, saved.get().getId());
        assertEquals(uuid, saved.get().getProperty().getId());
        assertEquals(uuid, saved.get().getGuest().getId());
        assertEquals(LocalDate.of(2025,12,20), saved.get().getDateRange().getStartDate());
        assertEquals(LocalDate.of(2025,12,25), saved.get().getDateRange().getEndDate());
    }
    @Test
    public void shouldReturnEmptyWhenSearchingForAReservationThatNotExists() {
        var savedBooking = bookingRepository.findById(UUID.randomUUID());
        assertTrue(savedBooking.isEmpty());
    }

    @Test
    public void shouldSaveReservationSuccessfulyAndMakeCancelation() {
        var uuid = UUID.randomUUID();
        var property = new Property(uuid, "Casa na Praia", "Vista para o mar", 6, 200);
        propertyRepository.save(property);

        var user = new User(uuid, "Carlos");
        userRepository.save(user);

        var dateRange = new DateRange(
                LocalDate.of(2024, 12, 20),
                LocalDate.of(2024, 12, 25)
        );

        var booking = new Booking(uuid, property, user, dateRange.getStartDate(), dateRange.getEndDate(), 4);
        bookingRepository.save(booking);


        booking.cancel(LocalDate.of(2024, 12, 15));
        bookingRepository.save(booking);

        var updatedBooking = bookingRepository.findById(uuid);
        assertTrue(updatedBooking.isPresent());
        assertEquals(Status.CANCELLED, updatedBooking.get().getStatus());
    }
}