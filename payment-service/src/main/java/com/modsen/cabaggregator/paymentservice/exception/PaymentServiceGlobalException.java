package com.modsen.cabaggregator.paymentservice.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentServiceGlobalException extends RuntimeException {

    private static final String GLOBAL_CODE = "Something went wrong";

    private final String code;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    PaymentServiceGlobalException(String message, String code) {
        super(message);
        this.code = code;
        this.message = message;
    }

    PaymentServiceGlobalException(String message) {
        super(message);
        this.code = GLOBAL_CODE;
        this.message = message;
    }

}
