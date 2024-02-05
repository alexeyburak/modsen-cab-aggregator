package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripeBalanceRetrieveException extends StripeGlobalException {

    public StripeBalanceRetrieveException(StripeException ex) {
        super("Balance retrieve error", ex.getStripeError(), ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
