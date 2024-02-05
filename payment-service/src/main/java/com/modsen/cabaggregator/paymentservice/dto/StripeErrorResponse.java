package com.modsen.cabaggregator.paymentservice.dto;

import com.stripe.model.StripeError;
import lombok.Builder;

@Builder
public record StripeErrorResponse(
        String message,
        StripeError stripeError,
        String code,
        String requestId
) {
}
