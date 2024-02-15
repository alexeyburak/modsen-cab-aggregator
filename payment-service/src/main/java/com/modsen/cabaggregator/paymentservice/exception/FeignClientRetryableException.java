package com.modsen.cabaggregator.paymentservice.exception;

public final class FeignClientRetryableException extends PaymentServiceGlobalException {
    public FeignClientRetryableException() {
        super("Error fetching third parties apis");
    }
}

