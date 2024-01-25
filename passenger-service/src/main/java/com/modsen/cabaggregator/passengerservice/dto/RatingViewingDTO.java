package com.modsen.cabaggregator.passengerservice.dto;

import com.modsen.cabaggregator.passengerservice.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingViewingDTO {
    private Passenger passenger;
    private Integer score;
    private UUID driverId;
}
