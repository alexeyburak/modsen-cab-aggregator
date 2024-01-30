package com.modsen.cabaggregator.rideservice.dto;

import com.modsen.cabaggregator.rideservice.model.enumeration.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponse {
    private UUID id;
    private String pickUp;
    private LocalDate date;
    private String destination;
    private UUID passengerId;
    private UUID driverId;
    private BigDecimal initialCost;
    private BigDecimal finalCost;
    private RideStatus status;
}
