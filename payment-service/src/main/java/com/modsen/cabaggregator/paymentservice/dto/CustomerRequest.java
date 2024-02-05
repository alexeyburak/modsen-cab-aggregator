package com.modsen.cabaggregator.paymentservice.dto;

import com.modsen.cabaggregator.common.util.GlobalConstants;
import jakarta.validation.constraints.Email;
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
    @NotBlank(message = "{customer.name.invalid.empty}")
    private String name;

    @Email(message = "{customer.email.invalid.pattern}")
    @NotBlank(message = "{customer.email.invalid.empty}")
    private String email;

    @Pattern(regexp = GlobalConstants.PHONE_REGEXP, message = "{customer.phone.invalid.pattern}")
    @NotBlank(message = "{customer.phone.invalid.empty}")
    private String phone;

    @NotNull(message = "{customer.passengerId.invalid.empty}")
    private UUID passengerId;

    @NotNull(message = "{customer.balance.invalid.empty}")
    private BigDecimal balance;
}
