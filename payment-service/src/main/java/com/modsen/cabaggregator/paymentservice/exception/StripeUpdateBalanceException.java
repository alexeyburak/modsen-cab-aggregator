package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripeUpdateBalanceException extends StripeGlobalException {

    public StripeUpdateBalanceException(StripeException ex) {
        super("Balance updating error", ex.getStripeError(), ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
