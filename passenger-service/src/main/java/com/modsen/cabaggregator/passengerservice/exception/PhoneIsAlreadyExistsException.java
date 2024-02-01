package com.modsen.cabaggregator.passengerservice.exception;

public final class PhoneIsAlreadyExistsException extends PassengerServiceGlobalException {
    public PhoneIsAlreadyExistsException(String phone) {
        super(String.format("Phone %s is already exists", phone));
    }
}
