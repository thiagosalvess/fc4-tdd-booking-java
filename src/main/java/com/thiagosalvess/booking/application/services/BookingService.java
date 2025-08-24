package com.thiagosalvess.booking.application.services;

import com.thiagosalvess.booking.application.dtos.CreateBookingDto;
import com.thiagosalvess.booking.domain.entities.Booking;
import com.thiagosalvess.booking.domain.exception.BookingNotFoundException;
import com.thiagosalvess.booking.domain.exception.PropertyNotFoundException;
import com.thiagosalvess.booking.domain.exception.UserNotFoundException;
import com.thiagosalvess.booking.domain.repositories.BookingRepository;
import com.thiagosalvess.booking.domain.valueobject.DateRange;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private PropertyService propertyService;
    private UserService userService;

    public BookingService(BookingRepository bookingRepository, PropertyService propertyService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.propertyService = propertyService;
        this.userService = userService;
    }

    public Booking createBooking(CreateBookingDto dto) {
        var property = this.propertyService.findPropertyById(dto.propertyId())
                .orElseThrow(PropertyNotFoundException::new);
        var guest = this.userService.findUserById(dto.guestId())
                .orElseThrow(UserNotFoundException::new);


        var booking = new Booking(
                UUID.randomUUID(),
                property,
                guest,
                dto.startDate(),
                dto.endDate(),
                dto.guestCount()
        );

        this.bookingRepository.save(booking);
        return booking;
    }

    public Optional<Booking> findById(UUID id) {
        return bookingRepository.findById(id);
    }

    public void cancelBooking(UUID id) {
        var booking = this.bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);

        booking.cancel(LocalDate.now());
        this.bookingRepository.save(booking);
    }
}
