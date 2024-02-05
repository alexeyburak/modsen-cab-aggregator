package com.modsen.cabaggregator.paymentservice.dto;

import lombok.Builder;

@Builder
public record ChargeResponse(
        String id,
        String currency,
        long amount
) {
}
