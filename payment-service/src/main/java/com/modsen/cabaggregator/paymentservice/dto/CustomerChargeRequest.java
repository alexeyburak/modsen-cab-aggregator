package com.modsen.cabaggregator.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerChargeRequest {
    @NotNull(message = "{charge.id.invalid.empty}")
    private UUID rideId;
}
