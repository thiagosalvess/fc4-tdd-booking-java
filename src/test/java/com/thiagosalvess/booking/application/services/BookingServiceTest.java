package com.thiagosalvess.booking.application.services;

import com.thiagosalvess.booking.application.dtos.CreateBookingDto;
import com.thiagosalvess.booking.domain.entities.Booking;
import com.thiagosalvess.booking.domain.entities.Property;
import com.thiagosalvess.booking.domain.entities.Status;
import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.domain.exception.BookingNotFoundException;
import com.thiagosalvess.booking.domain.exception.PropertyNotFoundException;
import com.thiagosalvess.booking.domain.exception.PropertyUnavailableException;
import com.thiagosalvess.booking.domain.exception.UserNotFoundException;
import com.thiagosalvess.booking.domain.repositories.PropertyRepository;
import com.thiagosalvess.booking.domain.repositories.UserRepository;
import com.thiagosalvess.booking.infrastructure.repositories.FakeBookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    private FakeBookingRepository fakeBookingRepository;

    @Mock
    private PropertyService mockPropertyService;

    @Mock
    private UserService mockUserService;

    @Mock
    private Property mockProperty;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        var mockPropertyRepository = Mockito.mock(PropertyRepository.class);
        var mockUserRepository = Mockito.mock(UserRepository.class);

        mockPropertyService = new PropertyService(mockPropertyRepository);
        mockUserService = new UserService(mockUserRepository);

        fakeBookingRepository = Mockito.spy(new FakeBookingRepository());
        bookingService = new BookingService(fakeBookingRepository, mockPropertyService, mockUserService);
    }

    @Test
    public void shouldCreateABookingSuccessfulyUsingFakeRepositorie() {
        var uuid = UUID.randomUUID();
        when(mockProperty.isAvailable(any())).thenReturn(true);
        when(mockProperty.calculateTotalPrice(any())).thenReturn(500.0);

        when(mockPropertyService.findPropertyById(any())).thenReturn(Optional.of(mockProperty));
        when(mockUserService.findUserById(any())).thenReturn(Optional.of(mockUser));

        var bookingDto = new CreateBookingDto(
                uuid,
                uuid,
                LocalDate.of(2025, 12, 20),
                LocalDate.of(2025, 12, 25),
                2
        );

        var result = bookingService.createBooking(bookingDto);

        assertInstanceOf(Booking.class, result);
        assertEquals(Status.CONFIRMED, result.getStatus());
        assertEquals(500, result.getTotalPrice());

        var savedBooking = bookingService.findById(result.getId());
        assertNotNull(savedBooking);
        assertEquals(savedBooking.get().getId(), result.getId());

    }

    @Test
    public void shouldThrowAnErrorWhenThePropertyNotFound() throws Exception {
        when(mockPropertyService.findPropertyById(any())).thenReturn(Optional.empty());
        var uuid = UUID.randomUUID();

        var bookingDto = new CreateBookingDto(
                uuid,
                uuid,
                LocalDate.of(2025, 12, 20),
                LocalDate.of(2025, 12, 25),
                2
        );

        PropertyNotFoundException ex = assertThrows(PropertyNotFoundException.class, () -> bookingService.createBooking(bookingDto));
        assertEquals("Propriedade não encontrada!", ex.getMessage());
    }

    @Test
    public void shouldThrowAnErrorWhenTheUserNotFound() throws Exception {
        var uuid = UUID.randomUUID();

        when(mockPropertyService.findPropertyById(any())).thenReturn(Optional.of(mockProperty));
        when(mockUserService.findUserById(any())).thenReturn(Optional.empty());

        var bookingDto = new CreateBookingDto(
                uuid,
                uuid,
                LocalDate.of(2025, 12, 20),
                LocalDate.of(2025, 12, 25),
                2
        );

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> bookingService.createBooking(bookingDto));
        assertEquals("Usuário não encontrado!", ex.getMessage());
    }

    @Test
    public void shouldThrowAnErrorWhenTryingToCreateAReservationToAPeriodAlreadyReservated() {
        when(mockProperty.isAvailable(any())).thenReturn(true);
        when(mockProperty.calculateTotalPrice(any())).thenReturn(500.0);

        when(mockPropertyService.findPropertyById(any())).thenReturn(Optional.of(mockProperty));
        when(mockUserService.findUserById(any())).thenReturn(Optional.of(mockUser));

        var uuid = UUID.randomUUID();

        var bookingDto = new CreateBookingDto(
                uuid,
                uuid,
                LocalDate.of(2025, 12, 20),
                LocalDate.of(2025, 12, 25),
                2
        );

        bookingService.createBooking(bookingDto);
        when(mockProperty.isAvailable(any())).thenReturn(false);

        PropertyUnavailableException ex = assertThrows(PropertyUnavailableException.class, () -> bookingService.createBooking(bookingDto));
        assertEquals("A propriedade não está disponível para o período selecionado!", ex.getMessage());
    }

    @Test
    public void shouldCancelAnExistingReservationUsingFakeRepository() {
        var uuid = UUID.randomUUID();
        when(mockProperty.isAvailable(any())).thenReturn(true);
        when(mockProperty.calculateTotalPrice(any())).thenReturn(500.0);

        when(mockPropertyService.findPropertyById(any())).thenReturn(Optional.of(mockProperty));
        when(mockUserService.findUserById(any())).thenReturn(Optional.of(mockUser));

        var bookingDto = new CreateBookingDto(
                uuid,
                uuid,
                LocalDate.of(2025, 12, 20),
                LocalDate.of(2025, 12, 25),
                2
        );

        var booking = bookingService.createBooking(bookingDto);

        bookingService.cancelBooking(booking.getId());

        var canceledBooking = bookingService.findById(booking.getId());

        assertEquals(Status.CANCELLED, canceledBooking.get().getStatus());
        verify(fakeBookingRepository, times(2)).findById(eq(booking.getId()));
    }

    @Test
    public void shouldThrowAnErrorWhenTryToCancelAReservationThatNotExists() {
        BookingNotFoundException ex = assertThrows(BookingNotFoundException.class, () -> bookingService.cancelBooking(UUID.randomUUID()));
        assertEquals("Reserva não encontrada!", ex.getMessage());
    }

}