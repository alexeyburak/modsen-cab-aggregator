package com.modsen.cabaggregator.rideservice.exception;

public final class PromoCodeNotFoundException extends RideServiceGlobalException {
    public PromoCodeNotFoundException(String name) {
        super(String.format("Promo code with name %s was not found", name));
    }
}
