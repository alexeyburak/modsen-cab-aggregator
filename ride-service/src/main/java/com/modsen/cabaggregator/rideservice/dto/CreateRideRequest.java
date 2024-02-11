package com.modsen.cabaggregator.rideservice.dto;

import com.modsen.cabaggregator.rideservice.model.enumeration.RidePaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateRideRequest {
    @NotNull(message = "{ride.invalid.passenger}")
    private UUID passengerId;

    @NotBlank(message = "{ride.invalid.pickUp}")
    private String pickUp;

    @NotBlank(message = "{ride.invalid.destination}")
    private String destination;
    private String promoCode;

    @NotNull(message = "{ride.invalid.paymentMethod}")
    private RidePaymentMethod paymentMethod;
}
