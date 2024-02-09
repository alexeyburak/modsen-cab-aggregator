package com.modsen.cabaggregator.paymentservice.exception;

import java.util.UUID;

public final class CustomerNotFoundException extends PaymentServiceGlobalException {
    public CustomerNotFoundException(UUID id) {
        super(String.format("Customer with id %s was not found", id));
    }
}
