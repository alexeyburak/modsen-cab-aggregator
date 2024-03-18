package com.modsen.cabaggregator.paymentservice.service.impl;

import com.modsen.cabaggregator.paymentservice.client.RideServiceClient;
import com.modsen.cabaggregator.paymentservice.dto.DriverResponse;
import com.modsen.cabaggregator.paymentservice.dto.PassengerResponse;
import com.modsen.cabaggregator.paymentservice.dto.RideInfoResponse;
import com.modsen.cabaggregator.paymentservice.model.enumeration.RidePaymentMethod;
import com.modsen.cabaggregator.paymentservice.model.enumeration.RideStatus;
import com.modsen.cabaggregator.paymentservice.service.RideService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@CircuitBreaker(name = "default", fallbackMethod = "fallbackRideService")
@Retry(name = "default")
public class RideServiceImpl implements RideService {

    private final RideServiceClient rideServiceClient;

    @Override
    public RideInfoResponse changeStatus(UUID id) {
        return rideServiceClient.changeStatus(id);
    }

    @Override
    public RideInfoResponse getRideById(UUID id) {
        return rideServiceClient.getRideById(id);
    }

    public RideInfoResponse fallbackRideService(UUID id, Exception ex) {
        log.info("Exception during request to Ride Service: {}", ex.getMessage());
        return RideInfoResponse.builder()
                .id(id)
                .pickUp("")
                .dateAt(LocalDate.now())
                .timeAt(LocalTime.now())
                .destination("")
                .passenger(new PassengerResponse())
                .driver(new DriverResponse())
                .initialCost(BigDecimal.ZERO)
                .finalCost(BigDecimal.ZERO)
                .status(RideStatus.REJECTED)
                .paymentMethod(RidePaymentMethod.CARD)
                .paid(false)
                .build();
    }

}
