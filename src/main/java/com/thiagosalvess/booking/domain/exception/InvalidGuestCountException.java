package com.thiagosalvess.booking.domain.exception;

public class InvalidGuestCountException extends RuntimeException {
    public InvalidGuestCountException() {
        super("O número de hóspedes deve ser maior que zero!");
    }
}
