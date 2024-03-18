package com.modsen.cabaggregator.rideservice.service;

import com.modsen.cabaggregator.rideservice.dto.PassengerResponse;

import java.util.UUID;

public interface PassengerService {
    PassengerResponse findById(UUID id);
}
