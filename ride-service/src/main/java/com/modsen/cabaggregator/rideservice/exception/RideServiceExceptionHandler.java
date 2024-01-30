package com.modsen.cabaggregator.rideservice.exception;

import com.modsen.cabaggregator.rideservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class RideServiceExceptionHandler {

    @ExceptionHandler(RideServiceGlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(RideServiceGlobalException ex) {
        final ErrorResponse response = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(ex.getTimestamp())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();

            errors.compute(field, (key, value) -> {
                if (Objects.nonNull(value)) {
                    value.add(fieldError.getDefaultMessage());
                    return value;
                }
                return new ArrayList<>(List.of(Objects.requireNonNull(fieldError.getDefaultMessage())));
            });
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
