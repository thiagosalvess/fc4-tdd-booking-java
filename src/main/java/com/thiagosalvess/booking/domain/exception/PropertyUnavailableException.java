package com.thiagosalvess.booking.domain.exception;

import com.thiagosalvess.booking.domain.valueobject.DateRange;

public class PropertyUnavailableException extends RuntimeException {
    public PropertyUnavailableException() {
        super("A propriedade não está disponível para o período selecionado!");
    }
}
