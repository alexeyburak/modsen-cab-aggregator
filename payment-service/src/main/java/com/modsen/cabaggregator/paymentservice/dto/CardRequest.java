package com.modsen.cabaggregator.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequest {
    @NotBlank(message = "{card.number.invalid.empty}")
    private String cardNumber;

    @NotNull(message = "{card.expMonth.invalid.empty}")
    private int expMonth;

    @NotNull(message = "{card.expYear.invalid.empty}")
    private int expYear;

    @NotBlank(message = "{card.cvc.invalid.empty}")
    @Length(max = 3, message = "{card.cvc.invalid.length}")
    private String cvc;
}
