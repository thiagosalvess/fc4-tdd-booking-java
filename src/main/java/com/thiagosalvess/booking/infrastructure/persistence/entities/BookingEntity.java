package com.thiagosalvess.booking.infrastructure.persistence.entities;

import com.thiagosalvess.booking.domain.entities.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor()
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private PropertyEntity property;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private UserEntity guest;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "guest_count")
    private int guestCount;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Column(name = "total_price")
    private double totalPrice;
}
