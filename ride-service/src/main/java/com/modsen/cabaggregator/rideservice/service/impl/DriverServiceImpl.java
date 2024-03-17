package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.rideservice.client.DriverServiceClient;
import com.modsen.cabaggregator.rideservice.dto.DriverResponse;
import com.modsen.cabaggregator.rideservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.rideservice.service.DriverService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@CircuitBreaker(name = "default", fallbackMethod = "fallbackDriverService")
@Retry(name = "default")
public class DriverServiceImpl implements DriverService {

    private final DriverServiceClient driverClient;

    @Override
    public UUID getAvailableDriverId() {
        return driverClient.findAvailableDriverById()
                .getId();
    }

    @Override
    public void changeDriverStatus(UUID id, DriverStatus status) {
        DriverResponse response = driverClient.updateStatus(id, status);
        log.info("Change driver status. ID: {}, New status: {}", response.getId(), response.getStatus());
    }

    @Override
    public DriverResponse findById(UUID id) {
        return driverClient.findById(id);
    }

    public UUID fallbackDriverService(Exception ex) {
        log.info("Exception during getAvailableDriverId request to Driver Service: {}", ex.getMessage());
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    public void fallbackDriverService(UUID id, DriverStatus status, Exception ex) {
        log.info("Unable change driver status. ID: {}, New status: {}, Exception: {}", id, status, ex.getMessage());
    }

    public DriverResponse fallbackDriverService(UUID id, Exception ex) {
        log.info("Exception during findById request to Driver Service: {}", ex.getMessage());
        return DriverResponse.builder()
                .id(id)
                .name("")
                .surname("")
                .phone("")
                .status(DriverStatus.UNAVAILABLE)
                .active(false)
                .build();
    }

}
