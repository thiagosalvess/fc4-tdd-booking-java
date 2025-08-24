package com.thiagosalvess.booking.domain.exception;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException() {
        super("Propriedade não encontrada!");
    }
}
