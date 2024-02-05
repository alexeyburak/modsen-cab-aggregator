package com.modsen.cabaggregator.paymentservice.exception;

public final class InsufficientBalanceException extends PaymentServiceGlobalException {
    public InsufficientBalanceException(long amount) {
        super(String.format("Insufficient balance. Transaction amount %s", amount));
    }
}
