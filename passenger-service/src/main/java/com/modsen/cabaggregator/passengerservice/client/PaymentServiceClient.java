package com.modsen.cabaggregator.passengerservice.client;

import com.modsen.cabaggregator.passengerservice.dto.CustomerRequest;
import com.modsen.cabaggregator.passengerservice.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "payment-service",
        url = "http://localhost:8084/api/v1/payments"
)
public interface PaymentServiceClient {

    @PostMapping("/customers")
    CustomerResponse createCustomer(@RequestBody CustomerRequest request);

}
