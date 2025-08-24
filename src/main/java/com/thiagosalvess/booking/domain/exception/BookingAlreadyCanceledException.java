package com.thiagosalvess.booking.domain.exception;

public class BookingAlreadyCanceledException extends RuntimeException {
    public BookingAlreadyCanceledException() {
        super("A reserva já está cancelada!");
    }
}
