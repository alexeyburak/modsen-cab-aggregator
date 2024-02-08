package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.exception.StripeException;

public final class StripePaymentMethodCreationException extends StripeGlobalException {

    public StripePaymentMethodCreationException(StripeException ex) {
        super("Payment method creation error", ex.getCode(), ex.getCode(), ex.getStatusCode());
    }

}
