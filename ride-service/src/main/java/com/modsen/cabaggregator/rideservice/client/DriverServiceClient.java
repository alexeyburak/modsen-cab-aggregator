package com.modsen.cabaggregator.rideservice.client;

import com.modsen.cabaggregator.rideservice.dto.DriverResponse;
import com.modsen.cabaggregator.rideservice.model.enumeration.DriverStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        value = "driver-service",
        url = "http://localhost:8082/api/v1/drivers"
)
public interface DriverServiceClient {

    @GetMapping("/available")
    DriverResponse findAvailableDriverById();

    @PatchMapping("/{id}/status")
    DriverResponse updateStatus(@PathVariable UUID id, @RequestParam DriverStatus status);

    @GetMapping("/{id}")
    DriverResponse findById(@PathVariable UUID id);

}
