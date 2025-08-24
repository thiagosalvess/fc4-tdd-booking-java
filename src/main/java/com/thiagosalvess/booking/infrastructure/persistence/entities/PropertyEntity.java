package com.thiagosalvess.booking.infrastructure.persistence.entities;

import com.thiagosalvess.booking.domain.entities.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor()
@AllArgsConstructor
@Entity
@Table(name = "properties")
public class PropertyEntity {
    @Id
    private UUID id;
    private String name;
    private String description;
    @Column(name = "max_guests")
    private int maxGuests;
    @Column(name = "base_price_per_night")
    private double basePricePerNight;
    @OneToMany(mappedBy = "property")
    private List<BookingEntity> bookings = new ArrayList<>();
}
