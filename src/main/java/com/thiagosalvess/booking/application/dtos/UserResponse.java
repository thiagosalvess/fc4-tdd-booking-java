package com.thiagosalvess.booking.application.dtos;

import com.thiagosalvess.booking.domain.entities.User;

import java.util.UUID;

public record UserResponse(UUID id, String name) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName());
    }
}
