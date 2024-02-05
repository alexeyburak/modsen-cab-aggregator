package com.modsen.cabaggregator.driverservice.dto;

import com.modsen.cabaggregator.common.util.GlobalConstants;
import com.modsen.cabaggregator.driverservice.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class CreateDriverRequest {
    @NotBlank(message = "{name.invalid.empty}")
    private String name;

    @NotBlank(message = "{surname.invalid.empty}")
    private String surname;

    @Schema(example = "80291112233")
    @Pattern(regexp = GlobalConstants.PHONE_REGEXP, message = "{phone.invalid.pattern}")
    @NotBlank(message = "{phone.invalid.empty}")
    private String phone;
}
