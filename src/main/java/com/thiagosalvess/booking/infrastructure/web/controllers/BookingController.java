package com.thiagosalvess.booking.infrastructure.web.controllers;

import com.thiagosalvess.booking.application.dtos.BookingResponse;
import com.thiagosalvess.booking.application.dtos.CreateBookingDto;
import com.thiagosalvess.booking.application.services.BookingService;
import com.thiagosalvess.booking.domain.entities.Booking;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody CreateBookingDto body) {
        Booking booking = bookingService.createBooking(body);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Booking created successfully",
                        "booking", BookingResponse.from(booking)
                ));
    }

    @Transactional
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable("id") UUID id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
