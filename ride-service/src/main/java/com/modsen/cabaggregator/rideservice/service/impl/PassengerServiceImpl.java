package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.rideservice.client.PassengerServiceClient;
import com.modsen.cabaggregator.rideservice.dto.PassengerResponse;
import com.modsen.cabaggregator.rideservice.service.PassengerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@CircuitBreaker(name = "default", fallbackMethod = "fallbackPassengerService")
@Retry(name = "default")
public class PassengerServiceImpl implements PassengerService {

    private final PassengerServiceClient passengerServiceClient;

    @Override
    public PassengerResponse findById(UUID id) {
        return passengerServiceClient.findById(id);
    }

    public PassengerResponse fallbackPassengerService(UUID id, Exception ex) {
        log.info("Exception during getAvailableDriverId request to Driver Service: {}", ex.getMessage());
        return PassengerResponse.builder()
                .id(id)
                .name("")
                .surname("")
                .email("")
                .phone("")
                .active(false)
                .build();
    }

}
