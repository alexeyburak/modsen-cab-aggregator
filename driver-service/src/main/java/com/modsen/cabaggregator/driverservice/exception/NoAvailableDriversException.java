package com.modsen.cabaggregator.driverservice.exception;

public final class NoAvailableDriversException extends DriverServiceGlobalException {
    public NoAvailableDriversException() {
        super("No available drivers at the moment");
    }
}
