package com.thiagosalvess.booking.domain.cancelation;

public class PartialRefund implements RefundRule {
    @Override
    public double calculateRefund(double totalPrice) {
        return totalPrice * 0.5;
    }
}
