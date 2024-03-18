package com.modsen.cabaggregator.paymentservice.service;

import com.modsen.cabaggregator.paymentservice.dto.RideInfoResponse;

import java.util.UUID;

public interface RideService {
    RideInfoResponse changeStatus(UUID id);

    RideInfoResponse getRideById(UUID id);
}
