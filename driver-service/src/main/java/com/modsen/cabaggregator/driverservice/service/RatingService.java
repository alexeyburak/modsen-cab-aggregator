package com.modsen.cabaggregator.driverservice.service;

import com.modsen.cabaggregator.driverservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.driverservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.driverservice.dto.RatingResponse;

import java.util.UUID;

public interface RatingService {
    RatingResponse rate(CreateRatingRequest createRatingRequest, UUID id);

    AllRatingsResponse getRatingsByDriverId(UUID id);

    AverageRatingResponse getAverageDriverRating(UUID id);
}
