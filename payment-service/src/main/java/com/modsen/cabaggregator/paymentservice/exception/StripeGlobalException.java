package com.modsen.cabaggregator.paymentservice.exception;

import lombok.Getter;

@Getter
public class StripeGlobalException extends PaymentServiceGlobalException {

    private final String code;
    private final String requestId;
    private final Integer statusCode;

    StripeGlobalException(String message,
                          String code,
                          String requestId,
                          Integer statusCode) {
        super(message);
        this.code = code;
        this.requestId = requestId;
        this.statusCode = statusCode;
    }

}
