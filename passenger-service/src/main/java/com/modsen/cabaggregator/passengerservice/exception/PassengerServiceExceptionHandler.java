package com.modsen.cabaggregator.passengerservice.exception;

import com.modsen.cabaggregator.passengerservice.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
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
public final class PassengerServiceExceptionHandler {

    @ExceptionHandler(PassengerServiceGlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(PassengerServiceGlobalException ex) {
        final ErrorResponse response = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(ex.getTimestamp())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();

            if (errors.containsKey(field)) {
                List<String> errorMessages = errors.get(field);
                errorMessages.add(fieldError.getDefaultMessage());
                errors.put(fieldError.getField(), errorMessages);
            } else {
                errors.put(fieldError.getField(), new ArrayList<>(
                        List.of(Objects.requireNonNull(fieldError.getDefaultMessage())))
                );
            }
        }
        return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
