package com.modsen.cabaggregator.paymentservice.dto;

import com.modsen.cabaggregator.paymentservice.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {
    @NotBlank(message = "Name can't be empty")
    private String name;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email can't be empty")
    private String email;

    @Pattern(regexp = Constants.PHONE_PATTERN_REGEXP)
    @NotBlank(message = "Phone can't be empty")
    private String phone;

    @NotNull(message = "Passenger can't be empty")
    private UUID passengerId;

    @NotNull(message = "Balance can't be empty")
    private BigDecimal balance;
}
