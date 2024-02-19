package com.modsen.cabaggregator.rideservice.service;

import com.modsen.cabaggregator.rideservice.model.enumeration.DriverStatus;

import java.util.UUID;

public interface DriverService {
    UUID getAvailableDriverId();

    void changeDriverStatus(UUID id, DriverStatus status);
}
