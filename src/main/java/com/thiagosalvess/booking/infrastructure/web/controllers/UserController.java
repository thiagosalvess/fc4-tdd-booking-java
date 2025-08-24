package com.thiagosalvess.booking.infrastructure.web.controllers;

import com.thiagosalvess.booking.application.dtos.CreateUserDto;
import com.thiagosalvess.booking.application.dtos.UserResponse;
import com.thiagosalvess.booking.application.services.UserService;
import com.thiagosalvess.booking.domain.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto dto) {
        User user = userService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "User created successfully",
                        "user", UserResponse.from(user)
                ));
    }
}
