package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripeChargeFromCustomerException extends StripeGlobalException {

    public StripeChargeFromCustomerException(StripeException ex) {
        super("Charge from customer error", ex.getStripeError(), ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
