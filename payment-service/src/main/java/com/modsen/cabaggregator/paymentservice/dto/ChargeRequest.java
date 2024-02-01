package com.modsen.cabaggregator.paymentservice.dto;

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
public class ChargeRequest {
    @NotNull(message = "Amount can't be null")
    private BigDecimal amount;

    @NotBlank(message = "Token can't be null")
    private String cardToken;
}
