package com.modsen.cabaggregator.passengerservice.service;

import com.modsen.cabaggregator.passengerservice.dto.CustomerRequest;
import com.modsen.cabaggregator.passengerservice.dto.CustomerResponse;

public interface PaymentService {
    CustomerResponse createCustomer(CustomerRequest request);
}
