package com.thiagosalvess.booking.domain.exception;

public class MaxGuestsMustBePositiveException extends RuntimeException {
    public MaxGuestsMustBePositiveException() {
        super("O número máximo de hóspedes deve ser maior que zero!");
    }
}
