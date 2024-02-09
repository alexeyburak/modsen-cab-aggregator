package com.modsen.cabaggregator.paymentservice.exception;

import com.modsen.cabaggregator.common.dto.ErrorResponse;
import com.modsen.cabaggregator.common.exception.CabAggregatorGlobalException;
import com.modsen.cabaggregator.common.exception.GlobalExceptionResolver;
import com.modsen.cabaggregator.paymentservice.dto.StripeErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class PaymentServiceExceptionHandler {

    @ExceptionHandler(CabAggregatorGlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(CabAggregatorGlobalException ex) {
        return GlobalExceptionResolver.handleGlobalException(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return GlobalExceptionResolver.handleMethodArgumentNotValidException(ex);
    }

    @ExceptionHandler(StripeGlobalException.class)
    public ResponseEntity<StripeErrorResponse> handleStripeGlobalException(StripeGlobalException ex) {
        final HttpStatus status = HttpStatus.resolve(ex.getStatusCode());
        final StripeErrorResponse body = StripeErrorResponse.builder()
                .message(ex.getMessage())
                .code(ex.getStatusCode().toString())
                .requestId(ex.getRequestId())
                .build();
        return new ResponseEntity<>(
                body, Objects.requireNonNull(status)
        );
    }

}
