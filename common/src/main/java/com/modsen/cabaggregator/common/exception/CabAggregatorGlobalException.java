package com.modsen.cabaggregator.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CabAggregatorGlobalException extends RuntimeException {

    private final String code;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public CabAggregatorGlobalException(String message) {
        super(message);
        this.code = "Something went wrong";
        this.message = message;
    }

    public CabAggregatorGlobalException(String message, String code) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
