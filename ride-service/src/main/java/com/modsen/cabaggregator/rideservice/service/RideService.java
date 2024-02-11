package com.modsen.cabaggregator.rideservice.service;

import com.modsen.cabaggregator.rideservice.dto.AllRidesResponse;
import com.modsen.cabaggregator.rideservice.dto.MessageResponse;
import com.modsen.cabaggregator.rideservice.dto.RideSortCriteria;
import com.modsen.cabaggregator.rideservice.dto.CreateRideRequest;
import com.modsen.cabaggregator.rideservice.dto.RideResponse;

import java.util.UUID;

public interface RideService {
    AllRidesResponse findAllPassengerRides(UUID passengerId, Integer page, Integer size, RideSortCriteria sort);

    RideResponse getById(UUID id);

    void deleteById(UUID id);

    RideResponse save(CreateRideRequest rideDTO);

    MessageResponse rejectRide(UUID id);

    MessageResponse startRide(UUID id);

    MessageResponse finishRide(UUID id);

    RideResponse changePaymentStatus(UUID id);
}
