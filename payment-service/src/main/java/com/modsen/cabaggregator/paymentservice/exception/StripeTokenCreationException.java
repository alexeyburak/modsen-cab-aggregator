package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripeTokenCreationException extends StripeGlobalException {

    public StripeTokenCreationException(StripeException ex) {
        super("Token creation error", ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
