package com.thiagosalvess.booking.domain.exception;

public class PropertyBasePricePerNightRequiredException extends RuntimeException {
    public PropertyBasePricePerNightRequiredException(){
        super("O preço base por noite é obrigatório!");
    }
}
