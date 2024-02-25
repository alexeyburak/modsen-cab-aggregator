package com.modsen.cabaggregator.passengerservice.exception;

public final class ThirdPartyApiException extends PassengerServiceGlobalException {
    public ThirdPartyApiException() {
        super("Exception in third party API");
    }
}
