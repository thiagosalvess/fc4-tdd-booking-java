package com.thiagosalvess.booking.domain.cancelation;

public class NoRefund implements RefundRule{
    @Override
    public double calculateRefund(double totalPrice) {
        return totalPrice;
    }
}
