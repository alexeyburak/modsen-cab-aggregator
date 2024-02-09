package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripeChargeException extends StripeGlobalException {

    public StripeChargeException(StripeException ex) {
        super("Charge error", ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
