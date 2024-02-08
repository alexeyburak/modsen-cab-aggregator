package com.modsen.cabaggregator.rideservice.exception;

import java.util.UUID;

public final class RideWasNotStartedException extends RideServiceGlobalException {
    public RideWasNotStartedException(UUID id) {
        super(String.format("Ride with id %s was not started", id));
    }
}
