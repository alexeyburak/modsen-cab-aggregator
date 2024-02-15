package com.modsen.cabaggregator.rideservice.client;

import com.modsen.cabaggregator.rideservice.dto.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        value = "passenger-service",
        url = "http://localhost:8081/api/v1/passengers"
)
public interface PassengerServiceClient {

    @GetMapping("/{id}")
    PassengerResponse findById(@PathVariable UUID id);

}
