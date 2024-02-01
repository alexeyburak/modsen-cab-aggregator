package com.modsen.cabaggregator.passengerservice.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PassengerServiceGlobalException extends RuntimeException {

    private static final String GLOBAL_CODE = "Something went wrong";

    private final String code;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    PassengerServiceGlobalException(String message, String code) {
        super(message);
        this.code = code;
        this.message = message;
    }

    PassengerServiceGlobalException(String message) {
        super(message);
        this.code = GLOBAL_CODE;
        this.message = message;
    }

}
