package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripeCheckBalanceException extends StripeGlobalException {

    public StripeCheckBalanceException(StripeException ex) {
        super("Balance checking error", ex.getStripeError(), ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
