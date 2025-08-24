package com.thiagosalvess.booking.domain.entities;

import com.thiagosalvess.booking.domain.exception.GuestCountExceededException;
import com.thiagosalvess.booking.domain.exception.MaxGuestsMustBePositiveException;
import com.thiagosalvess.booking.domain.exception.PropertyBasePricePerNightRequiredException;
import com.thiagosalvess.booking.domain.exception.PropertyNameRequiredException;
import com.thiagosalvess.booking.domain.valueobject.DateRange;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Property {
    private UUID id;
    private String name;
    private String description;
    private int maxGuests;
    private double basePricePerNight;
    private List<Booking> bookings = new ArrayList<>();

    public Property(UUID id, String name, String description, int maxGuests, double basePricePerNight) {
        if (name.isBlank()) {
            throw new PropertyNameRequiredException();
        }
        if (maxGuests <= 0) {
            throw new MaxGuestsMustBePositiveException();
        }
        if (basePricePerNight <= 0) {
            throw new PropertyBasePricePerNightRequiredException();
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxGuests = maxGuests;
        this.basePricePerNight = basePricePerNight;
    }

    public void validateGuestCount(int guestCount) {
        if (guestCount > this.maxGuests) {
            throw new GuestCountExceededException(this.maxGuests);
        }
    }

    public double calculateTotalPrice(DateRange dateRange) {
        var totalNights = dateRange.getTotalNights();
        var totalPrice = totalNights * this.basePricePerNight;
        if (totalNights >= 7) {
            totalPrice *= 0.9;
        }
        return totalPrice;
    }

    public boolean isAvailable(DateRange dateRange) {
        return bookings.stream().noneMatch(booking ->
                booking.getStatus() == Status.CONFIRMED
                        && booking.getDateRange().overlaps(dateRange)
        );
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }
}
