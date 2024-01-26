package com.modsen.cabaggregator.passengerservice.exception;

public final class InvalidPageRequestException extends PassengerServiceGlobalException {
    public InvalidPageRequestException() {
        super("Invalid page request");
    }
}
