package com.modsen.cabaggregator.rideservice.exception;

import java.util.UUID;

public final class ImpossibleRideRejectionException extends RideServiceGlobalException {
    public ImpossibleRideRejectionException(UUID id) {
        super(String.format("Impossible to reject ride with id %s", id));
    }
}
