package com.thiagosalvess.booking.domain.exception;

public class UserIdRequiredException extends RuntimeException {
    public UserIdRequiredException() { super("O ID é obrigatório!"); }
}
