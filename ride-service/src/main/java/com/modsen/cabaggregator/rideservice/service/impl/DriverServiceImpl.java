package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.rideservice.client.DriverServiceClient;
import com.modsen.cabaggregator.rideservice.dto.DriverResponse;
import com.modsen.cabaggregator.rideservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.rideservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
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

}
