package com.modsen.cabaggregator.rideservice.service.impl;

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

    @Override
    public UUID getAvailableDriverId() {
        return UUID.randomUUID();
    }

    @Override
    public void changeDriverStatus(UUID id, DriverStatus driverStatus) {
        log.info("Change driver status. ID: {}, New status: {}", id, driverStatus);
    }

}
