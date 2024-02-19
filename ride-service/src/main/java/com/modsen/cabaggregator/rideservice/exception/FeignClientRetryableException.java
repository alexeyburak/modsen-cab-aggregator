package com.modsen.cabaggregator.rideservice.exception;

public final class FeignClientRetryableException extends RideServiceGlobalException {
    public FeignClientRetryableException() {
        super("Error fetching third parties apis");
    }
}

