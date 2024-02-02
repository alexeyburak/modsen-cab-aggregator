package com.modsen.cabaggregator.driverservice.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DriverServiceGlobalException extends RuntimeException {

    private static final String GLOBAL_CODE = "Something went wrong";

    private final String code;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    DriverServiceGlobalException(String message, String code) {
        super(message);
        this.code = code;
        this.message = message;
    }

    DriverServiceGlobalException(String message) {
        super(message);
        this.code = GLOBAL_CODE;
        this.message = message;
    }

}
