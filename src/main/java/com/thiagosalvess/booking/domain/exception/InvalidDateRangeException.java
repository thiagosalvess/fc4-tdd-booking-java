package com.thiagosalvess.booking.domain.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String message) { super(message); }

    public static InvalidDateRangeException startAfterEnd() {
        return new InvalidDateRangeException("A data de término deve ser posterior a data de início!");
    }
    public static InvalidDateRangeException startEqualsEnd() {
        return new InvalidDateRangeException("A data de início e término não podem ser iguais!");
    }
}
