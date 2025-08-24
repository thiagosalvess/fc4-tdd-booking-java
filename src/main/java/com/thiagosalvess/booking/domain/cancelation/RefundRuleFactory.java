package com.thiagosalvess.booking.domain.cancelation;

public class RefundRuleFactory {
    public static RefundRule getRefundRule(int daysUntilCheckin) {
        if (daysUntilCheckin > 7) {
            return new FullRefund();
        } else if (daysUntilCheckin >= 1) {
            return new PartialRefund();
        }
        return new NoRefund();
    }
}
