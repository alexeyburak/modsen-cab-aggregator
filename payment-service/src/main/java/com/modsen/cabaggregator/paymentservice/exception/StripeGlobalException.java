package com.modsen.cabaggregator.paymentservice.exception;

import com.stripe.model.StripeError;
import lombok.Getter;

@Getter
public class StripeGlobalException extends PaymentServiceGlobalException {

    private final StripeError stripeError;
    private final String code;
    private final String requestId;
    private final Integer statusCode;

    StripeGlobalException(String message,
                          StripeError stripeError,
                          String code,
                          String requestId,
                          Integer statusCode) {
        super(message);
        this.stripeError = stripeError;
        this.code = code;
        this.requestId = requestId;
        this.statusCode = statusCode;
    }

}
