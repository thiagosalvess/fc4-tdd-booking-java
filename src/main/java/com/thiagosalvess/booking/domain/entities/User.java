package com.thiagosalvess.booking.domain.entities;

import com.thiagosalvess.booking.domain.exception.UserIdRequiredException;
import com.thiagosalvess.booking.domain.exception.UserNameRequiredException;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID id;
    private String name;


    public User(UUID id, String name) {
        if (name.isBlank()) {
            throw new UserNameRequiredException();
        }
        if (id == null) {
            throw new UserIdRequiredException();
        }
        this.id = id;
        this.name = name;
    }
}
