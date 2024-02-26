package com.modsen.cabaggregator.paymentservice.exception;

public final class ThirdPartyApiException extends PaymentServiceGlobalException {
    public ThirdPartyApiException() {
        super("Exception in third party API");
    }
}
