package com.modsen.cabaggregator.driverservice.exception;

import com.modsen.cabaggregator.driverservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class DriverServiceExceptionHandler {

    @ExceptionHandler(DriverServiceGlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(DriverServiceGlobalException ex) {
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
        fieldErrors.forEach(fieldError -> {
            String field = fieldError.getField();

            errors.compute(field, (key, value) -> {
                if (Objects.nonNull(value)) {
                    value.add(fieldError.getDefaultMessage());
                    return value;
                }
                return new ArrayList<>(List.of(Objects.requireNonNull(fieldError.getDefaultMessage())));
            });
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
