package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripeCustomerRetrieveException extends StripeGlobalException {

    public StripeCustomerRetrieveException(StripeException ex) {
        super("Customer retrieve error", ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
