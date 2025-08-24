package com.thiagosalvess.booking.domain.valueobject;

import com.thiagosalvess.booking.domain.exception.InvalidDateRangeException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
public final class DateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRange of(LocalDate start, LocalDate end) {
        return new DateRange(start, end);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw InvalidDateRangeException.startAfterEnd();
        }
        if (endDate.isEqual(startDate)) {
            throw InvalidDateRangeException.startEqualsEnd();
        }
    }

    public int getTotalNights() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public boolean overlaps(DateRange other) {
        return this.startDate.isBefore(other.endDate) && other.startDate.isBefore(this.endDate);
    }
}
