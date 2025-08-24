package com.thiagosalvess.booking.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor()
@AllArgsConstructor
public class UserEntity {
    @Id
    private UUID id;
    private String name;
}
