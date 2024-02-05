package com.modsen.cabaggregator.common.exception;

import com.modsen.cabaggregator.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GlobalExceptionResolver {

    private GlobalExceptionResolver() {
    }

    public static ResponseEntity<ErrorResponse> handleGlobalException(CabAggregatorGlobalException ex) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage())
                        .timestamp(ex.getTimestamp())
                        .build()
                );
    }

    public static ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
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
