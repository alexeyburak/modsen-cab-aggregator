package com.modsen.cabaggregator.paymentservice.client;

import com.modsen.cabaggregator.paymentservice.dto.RideInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(
        value = "${feign.client.config.ride.name}",
        path = "${feign.client.config.ride.path}"
)
public interface RideServiceClient {

    @PostMapping("/{id}/pay")
    RideInfoResponse changeStatus(@PathVariable UUID id);

    @GetMapping("/{id}")
    RideInfoResponse getRideById(@PathVariable UUID id);
}
