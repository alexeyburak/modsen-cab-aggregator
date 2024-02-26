package com.modsen.cabaggregator.rideservice.exception;

public final class ThirdPartyEntityNotFoundException extends RideServiceGlobalException {
    public ThirdPartyEntityNotFoundException() {
        super("Third party entity was not found");
    }
}
