package com.modsen.cabaggregator.passengerservice.exception;

public final class EmailIsAlreadyExistsException extends PassengerServiceGlobalException {
    public EmailIsAlreadyExistsException(String email) {
        super(String.format("Email %s is already exists", email));
    }
}
