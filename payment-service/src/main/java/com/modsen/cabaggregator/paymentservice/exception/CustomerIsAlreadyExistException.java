package com.modsen.cabaggregator.paymentservice.exception;

public final class CustomerIsAlreadyExistException extends PaymentServiceGlobalException {
    public CustomerIsAlreadyExistException(String email) {
        super(String.format("Customer with email %s is already exists", email));
    }
}
