package com.thiagosalvess.booking.domain.valueobject;

import com.thiagosalvess.booking.domain.exception.InvalidDateRangeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateRangeTest {

    @Test
    public void shouldThrowAnErrorIfEndDateIsBeforeStartDate() throws Exception {
        LocalDate startDate = LocalDate.of(2025, 8, 20);
        LocalDate endDate = LocalDate.of(2025, 8, 19);

        InvalidDateRangeException ex = assertThrows(InvalidDateRangeException.class, () -> new DateRange(startDate, endDate));
        assertEquals("A data de término deve ser posterior a data de início!", ex.getMessage());
    }

    @Test
    public void shouldCreateAnInstansceOfDataRangeWithStartDateAndEndDateAndVerifyTheReturn() {
        LocalDate startDate = LocalDate.of(2025, 8, 20);
        LocalDate endDate = LocalDate.of(2025, 8, 25);
        var dataRange = new DateRange(startDate, endDate);

        assertEquals(startDate, dataRange.getStartDate());
        assertEquals(endDate, dataRange.getEndDate());
    }

    @Test
    public void shouldCalculateTheTotalNights() {
        LocalDate startDate = LocalDate.of(2025, 8, 20);
        LocalDate endDate = LocalDate.of(2025, 8, 25);
        var dataRange = new DateRange(startDate, endDate);

        var totalNights = dataRange.getTotalNights();
        assertEquals(5, totalNights);

        LocalDate startDate1 = LocalDate.of(2025, 8, 10);
        LocalDate endDate1 = LocalDate.of(2025, 8, 25);
        var dataRange1 = new DateRange(startDate1, endDate1);

        var totalNights1 = dataRange1.getTotalNights();
        assertEquals(15, totalNights1);
    }

    @Test
    public void shouldVerifyIfTwoIntervalsOfDateOverlap() {
        var dateRange1 = new DateRange(
                LocalDate.of(2025, 8, 20),
                LocalDate.of(2025, 8, 25)
        );
        var dateRange2 = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 27)
        );

        var overlaps = dateRange1.overlaps(dateRange2);
        assertEquals(true, overlaps);

        var dateRange3 = new DateRange(
                LocalDate.of(2025, 8, 20),
                LocalDate.of(2025, 8, 25)
        );
        var dateRange4 = new DateRange(
                LocalDate.of(2025, 8, 22),
                LocalDate.of(2025, 8, 27)
        );

        var overlaps1 = dateRange3.overlaps(dateRange4);
        assertEquals(true, overlaps1);
    }

    @Test
    public void shouldThrowErrorIfTheStartAndEndDatesAreEqual() throws Exception {
        var date = LocalDate.of(2025, 8, 20);

        InvalidDateRangeException ex = assertThrows(InvalidDateRangeException.class, () -> new DateRange(date, date));
        assertEquals("A data de início e término não podem ser iguais!", ex.getMessage());
    }

}