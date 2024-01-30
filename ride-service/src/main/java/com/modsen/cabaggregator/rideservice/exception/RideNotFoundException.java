package com.modsen.cabaggregator.rideservice.exception;

import java.util.UUID;

public final class RideNotFoundException extends RideServiceGlobalException {
    public RideNotFoundException(UUID id) {
        super(String.format("Ride with id %s was not found", id));
    }
}
