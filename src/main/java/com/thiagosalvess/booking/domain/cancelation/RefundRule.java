package com.thiagosalvess.booking.domain.cancelation;

public interface RefundRule {
    double calculateRefund(double totalPrice);
}
