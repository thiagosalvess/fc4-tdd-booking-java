package com.thiagosalvess.booking.domain.cancelation;

public class FullRefund implements RefundRule {
    @Override
    public double calculateRefund(double totalPrice) {
        return 0;
    }
}
