package com.modsen.cabaggregator.driverservice.exception;

import com.modsen.cabaggregator.common.dto.ErrorResponse;
import com.modsen.cabaggregator.common.exception.CabAggregatorGlobalException;
import com.modsen.cabaggregator.common.exception.GlobalExceptionResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public final class DriverServiceExceptionHandler {

    @ExceptionHandler(DriverServiceGlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(CabAggregatorGlobalException ex) {
        return GlobalExceptionResolver.handleGlobalException(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return GlobalExceptionResolver.handleMethodArgumentNotValidException(ex);
    }

}
