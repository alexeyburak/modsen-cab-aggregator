package com.modsen.cabaggregator.paymentservice.dto;

import lombok.Builder;

@Builder
public record StripeErrorResponse(
        String message,
        String code,
        String requestId
) {
}
