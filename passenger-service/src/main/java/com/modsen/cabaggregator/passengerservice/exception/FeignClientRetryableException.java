package com.modsen.cabaggregator.passengerservice.exception;

public final class FeignClientRetryableException extends PassengerServiceGlobalException {
    public FeignClientRetryableException() {
        super("Error fetching third parties apis");
    }
}

