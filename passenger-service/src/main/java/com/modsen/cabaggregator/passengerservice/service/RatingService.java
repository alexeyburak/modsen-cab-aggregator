package com.modsen.cabaggregator.passengerservice.service;

import com.modsen.cabaggregator.passengerservice.dto.AverageRatingDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingListDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingViewingDTO;

import java.util.UUID;

public interface RatingService {
    RatingViewingDTO rate(RatingDTO ratingDTO, UUID passengerId);
    RatingListDTO getRatingsByPassengerId(UUID passengerId);
    AverageRatingDTO getAveragePassengerRating(UUID passengerId);
}
