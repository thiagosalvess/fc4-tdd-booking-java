package com.thiagosalvess.booking.domain.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException() {
        super("Reserva não encontrada!");
    }

}
