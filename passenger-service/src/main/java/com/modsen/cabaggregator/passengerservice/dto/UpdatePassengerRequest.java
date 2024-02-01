package com.modsen.cabaggregator.passengerservice.dto;

import com.modsen.cabaggregator.passengerservice.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePassengerRequest {
    @NotBlank(message = "{name.invalid.empty}")
    private String name;

    @NotBlank(message = "{surname.invalid.empty}")
    private String surname;

    @Schema(example = "burakalexey@yahoo.com")
    @Email(message = "{email.invalid.pattern}")
    @NotBlank(message = "{email.invalid.empty}")
    private String email;

    @Schema(example = "80291112233")
    @Pattern(regexp = Constants.PHONE_REGEXP, message = "{phone.invalid.pattern}")
    @NotBlank(message = "{phone.invalid.empty}")
    private String phone;
}
