package com.thiagosalvess.booking.domain.exception;

public class GuestCountExceededException extends RuntimeException {
    public GuestCountExceededException(int maxGuests) {
        super("Número máximo de hóspedes excedido. Máximo permitido: " + maxGuests + ".");
    }
}
