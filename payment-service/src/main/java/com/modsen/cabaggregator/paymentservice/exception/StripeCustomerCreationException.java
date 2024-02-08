package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripeCustomerCreationException extends StripeGlobalException {

    public StripeCustomerCreationException(StripeException ex) {
        super("Customer creation error", ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
