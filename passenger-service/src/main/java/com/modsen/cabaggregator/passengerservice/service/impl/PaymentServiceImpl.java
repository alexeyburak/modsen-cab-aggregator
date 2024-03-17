package com.modsen.cabaggregator.passengerservice.service.impl;

import com.modsen.cabaggregator.passengerservice.client.PaymentServiceClient;
import com.modsen.cabaggregator.passengerservice.dto.CustomerRequest;
import com.modsen.cabaggregator.passengerservice.dto.CustomerResponse;
import com.modsen.cabaggregator.passengerservice.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@CircuitBreaker(name = "default", fallbackMethod = "fallbackPaymentService")
@Retry(name = "default")
public class PaymentServiceImpl implements PaymentService {

    private final PaymentServiceClient paymentServiceClient;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        return paymentServiceClient.createCustomer(request);
    }

    public CustomerResponse fallbackPaymentService(UUID id, Exception ex) {
        log.info("Exception during request to Payment Service: {}", ex.getMessage());
        return CustomerResponse.builder()
                .id(id.toString())
                .email("")
                .phone("")
                .description("")
                .balance(0)
                .name("")
                .build();
    }

}
