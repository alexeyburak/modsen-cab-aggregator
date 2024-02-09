package com.modsen.cabaggregator.paymentservice.dto;

import java.math.BigDecimal;

public record BalanceResponse(
        BigDecimal amount,
        String currency
) {
}
