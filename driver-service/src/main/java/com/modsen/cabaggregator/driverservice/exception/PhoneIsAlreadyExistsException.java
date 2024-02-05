package com.modsen.cabaggregator.driverservice.exception;

public final class PhoneIsAlreadyExistsException extends DriverServiceGlobalException {
    public PhoneIsAlreadyExistsException(String phone) {
        super(String.format("Driver with phone %s is already exists", phone));
    }
}
