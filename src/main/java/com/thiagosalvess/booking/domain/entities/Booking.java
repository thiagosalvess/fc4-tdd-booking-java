package com.thiagosalvess.booking.domain.entities;

import com.thiagosalvess.booking.domain.cancelation.RefundRuleFactory;
import com.thiagosalvess.booking.domain.exception.BookingAlreadyCanceledException;
import com.thiagosalvess.booking.domain.exception.InvalidGuestCountException;
import com.thiagosalvess.booking.domain.exception.PropertyUnavailableException;
import com.thiagosalvess.booking.domain.valueobject.DateRange;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Getter
@NoArgsConstructor
@Setter
public class Booking {
    private UUID id;
    private Property property;
    private User guest;
    private DateRange dateRange;
    private int guestCount;
    private Status status;
    private double totalPrice;

    public Booking(UUID id, Property property, User guest, LocalDate startDate, LocalDate endDate, int guestCount) {
        if (guestCount <= 0) {
            throw new InvalidGuestCountException();
        }
        property.validateGuestCount(guestCount);
        this.dateRange = DateRange.of(startDate, endDate);
        if (!property.isAvailable(dateRange)) {
            throw new PropertyUnavailableException();
        }
        this.id = id;
        this.property = property;
        this.guest = guest;
        this.guestCount = guestCount;
        this.totalPrice = property.calculateTotalPrice(dateRange);
        this.status = Status.CONFIRMED;

        property.addBooking(this);
    }

    public void cancel(LocalDate currentDate) {
        if (this.status == Status.CANCELLED) {
            throw new BookingAlreadyCanceledException();
        }

        var daysUntilCheckIn = (int) ChronoUnit.DAYS.between(currentDate, dateRange.getStartDate());

        var refundRule = RefundRuleFactory.getRefundRule(daysUntilCheckIn);
        this.totalPrice = refundRule.calculateRefund(this.totalPrice);
        this.status = Status.CANCELLED;
    }

}
