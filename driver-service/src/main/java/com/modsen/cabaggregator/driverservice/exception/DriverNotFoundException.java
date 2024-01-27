package com.modsen.cabaggregator.driverservice.exception;

import java.util.UUID;

public final class DriverNotFoundException extends DriverServiceGlobalException {
    public DriverNotFoundException(UUID id) {
        super(String.format("Driver with id %s was not found", id));
    }
}
