package com.modsen.cabaggregator.rideservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePromoCodeRequest {
    @NotBlank(message = "{promo.invalid.name}")
    private String name;

    @NotNull(message = "{promo.invalid.value}")
    private BigDecimal value;
}
