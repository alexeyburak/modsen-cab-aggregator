package com.modsen.cabaggregator.driverservice.exception;

public final class InvalidPageRequestException extends DriverServiceGlobalException {
    public InvalidPageRequestException() {
        super("Invalid page request params");
    }
}
