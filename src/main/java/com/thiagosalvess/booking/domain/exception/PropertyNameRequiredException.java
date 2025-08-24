package com.thiagosalvess.booking.domain.exception;

public class PropertyNameRequiredException extends RuntimeException {
    public PropertyNameRequiredException() {
        super("O nome é obrigatório!");
    }
}
