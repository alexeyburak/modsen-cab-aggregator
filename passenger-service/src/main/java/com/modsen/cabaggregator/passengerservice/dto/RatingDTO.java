package com.modsen.cabaggregator.passengerservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class RatingDTO {
    @NotNull(message = "{score.invalid.empty}")
    @Min(value = 1, message = "{score.invalid.min}")
    @Max(value = 5, message = "{score.invalid.max}")
    private Integer score;

    @NotNull(message = "{driver.invalid.empty}")
    private UUID driverId;
}
