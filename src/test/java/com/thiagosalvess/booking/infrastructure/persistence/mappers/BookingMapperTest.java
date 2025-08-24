package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.Booking;
import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.domain.entities.Status;
import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.domain.exception.InvalidDateRangeException;
import com.thiagosalvess.booking.domain.valueobject.DateRange;
import com.thiagosalvess.booking.infrastructure.persistence.entities.BookingEntity;
import com.thiagosalvess.booking.infrastructure.persistence.entities.PropertyEntity;
import com.thiagosalvess.booking.infrastructure.persistence.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        BookingMapperImpl.class,
        PropertyMapperImpl.class,
        UserMapperImpl.class
})
class BookingMapperTest {

    @Autowired
    private BookingMapperImpl bookingMapper;

    @Test
    public void shouldConvertBookingEntityToBookingCorrectly() {
        UUID id = UUID.randomUUID();
        UUID propertyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        PropertyEntity propertyEntity = createProperty(propertyId);
        UserEntity userEntity = createUserEntity(userId);
        BookingEntity bookingEntity = createBookingEntity(id, propertyEntity, userEntity);

        Booking domain = bookingMapper.toDomain(bookingEntity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(propertyId, domain.getProperty().getId());
        assertEquals(userId, domain.getGuest().getId());
        assertEquals(LocalDate.of(2025,12,20), domain.getDateRange().getStartDate());
        assertEquals(LocalDate.of(2025,12,25), domain.getDateRange().getEndDate());
        assertEquals(3, domain.getGuestCount());
        assertEquals(Status.CONFIRMED, domain.getStatus());
        assertEquals(1000, domain.getTotalPrice());
    }



    @Test
    public void shoulThrowAnErrorOfValidationWhenMissingRequiredPropertiesInBookingEntity() {
        UUID id = UUID.randomUUID();

        PropertyEntity propertyEntity = createProperty(UUID.randomUUID());

        UserEntity userEntity = createUserEntity(UUID.randomUUID());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(id);
        bookingEntity.setProperty(propertyEntity);
        bookingEntity.setGuest(userEntity);
        bookingEntity.setStartDate(LocalDate.of(2025,12,25)); // obrigatório
        bookingEntity.setEndDate(LocalDate.of(2025, 12, 25));
        bookingEntity.setGuestCount(2);

        assertThrows(InvalidDateRangeException.class, () -> bookingMapper.toDomain(bookingEntity));
    }

    @Test
    public void shouldConvertBookingToBookingEntityCorrectly() {
        UUID id = UUID.randomUUID();
        Property property = new Property(UUID.randomUUID(), "Chalé", "Serra", 4, 120);
        User guest = new User(UUID.randomUUID(), "Carlos");
        DateRange dateRange = DateRange.of(LocalDate.of(2025, 12, 20), LocalDate.of(2025, 12, 25));

        Booking booking = new Booking(id, property, guest, dateRange.getStartDate(), dateRange.getEndDate(), 2);

        BookingEntity entity = bookingMapper.toEntity(booking);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(booking.getProperty().getId(), entity.getProperty().getId());
        assertEquals(booking.getGuest().getId(), entity.getGuest().getId());
        assertEquals(dateRange.getStartDate(), entity.getStartDate());
        assertEquals(dateRange.getEndDate(), entity.getEndDate());
        assertEquals(2, entity.getGuestCount());
        assertEquals(booking.getStatus(), entity.getStatus());
        assertEquals(booking.getTotalPrice(), entity.getTotalPrice());
    }

    private PropertyEntity createProperty(UUID id) {

        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setId(id);
        propertyEntity.setName("Casa Beira mar");
        propertyEntity.setDescription("Beira mar");
        propertyEntity.setMaxGuests(5);
        propertyEntity.setBasePricePerNight(250);
        return propertyEntity;
    }

    private UserEntity createUserEntity(UUID id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName("João");
        return userEntity;
    }

    private BookingEntity createBookingEntity(UUID id, PropertyEntity propertyEntity, UserEntity userEntity) {
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(id);
        bookingEntity.setProperty(propertyEntity);
        bookingEntity.setGuest(userEntity);
        bookingEntity.setStartDate(LocalDate.of(2025, 12, 20));
        bookingEntity.setEndDate(LocalDate.of(2025, 12, 25));
        bookingEntity.setGuestCount(3);
        bookingEntity.setStatus(Status.CONFIRMED);
        bookingEntity.setTotalPrice(1000);
        return bookingEntity;
    }
}