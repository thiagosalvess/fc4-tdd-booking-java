package com.thiagosalvess.booking.domain.exception;

public class UserNameRequiredException extends RuntimeException {
    public UserNameRequiredException() { super("O nome é obrigatório!"); }
}
