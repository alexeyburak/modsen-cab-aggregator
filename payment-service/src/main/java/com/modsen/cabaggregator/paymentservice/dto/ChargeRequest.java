package com.modsen.cabaggregator.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ChargeRequest {
    @NotNull(message = "{charge.id.invalid.empty}")
    private UUID rideId;

    @NotBlank(message = "{charge.token.invalid.empty}")
    private String cardToken;
}
