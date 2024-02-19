package com.modsen.cabaggregator.passengerservice.dto;

import lombok.Builder;

@Builder
public record CustomerResponse(
        String id,
        String email,
        String phone,
        String description,
        long balance,
        String name
) {
}
