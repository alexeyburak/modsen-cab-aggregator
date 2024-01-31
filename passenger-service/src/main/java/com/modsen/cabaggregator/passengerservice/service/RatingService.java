package com.modsen.cabaggregator.passengerservice.service;

import com.modsen.cabaggregator.passengerservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.passengerservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.passengerservice.dto.RatingResponse;

import java.util.UUID;

public interface RatingService {
    RatingResponse rate(CreateRatingRequest createRatingRequest, UUID passengerId);

    AllRatingsResponse getRatingsByPassengerId(UUID passengerId);

    AverageRatingResponse getAveragePassengerRating(UUID passengerId);
}
