package com.modsen.cabaggregator.rideservice.dto;

import com.modsen.cabaggregator.rideservice.model.enumeration.RidePaymentMethod;
import com.modsen.cabaggregator.rideservice.model.enumeration.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideInfoResponse {
    private UUID id;
    private String pickUp;
    private LocalDate dateAt;
    private LocalTime timeAt;
    private String destination;
    private PassengerResponse passenger;
    private DriverResponse driver;
    private BigDecimal initialCost;
    private BigDecimal finalCost;
    private RideStatus status;
    private RidePaymentMethod paymentMethod;
    private boolean paid;
}
