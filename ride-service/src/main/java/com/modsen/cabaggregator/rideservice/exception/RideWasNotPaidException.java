package com.modsen.cabaggregator.rideservice.exception;

import java.util.UUID;

public final class RideWasNotPaidException extends RideServiceGlobalException {
    public RideWasNotPaidException(UUID id) {
        super(String.format("Ride with id %s was not paid", id));
    }
}
