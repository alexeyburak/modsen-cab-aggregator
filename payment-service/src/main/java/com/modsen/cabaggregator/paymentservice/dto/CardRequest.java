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
    @NotBlank(message = "Card number can't be empty")
    private String cardNumber;

    @NotNull(message = "Expiration month can't be empty")
    private int expMonth;

    @NotNull(message = "Expiration year can't be empty")
    private int expYear;

    @NotBlank(message = "Cvc can't be empty")
    @Length(max = 3,message = "Max length is 3")
    private String cvc;
}
