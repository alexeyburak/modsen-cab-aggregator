package com.modsen.cabaggregator.passengerservice.service.impl;

import com.modsen.cabaggregator.passengerservice.dto.AverageRatingDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingListDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingViewingDTO;
import com.modsen.cabaggregator.passengerservice.mapper.RatingMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.model.Rating;
import com.modsen.cabaggregator.passengerservice.repository.RatingRepository;
import com.modsen.cabaggregator.passengerservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultRatingService implements RatingService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRatingService.class);
    public static final int DEFAULT_SCORE = 5;
    public static final String DECIMAL_FORMAT_PATTERN = "#.#";

    private final RatingRepository ratingRepository;
    private final DefaultPassengerService passengerService;
    private final RatingMapper ratingMapper;

    @Override
    public RatingViewingDTO rate(RatingDTO ratingDTO, UUID passengerId) {
        final UUID ratingId = UUID.randomUUID();
        final UUID driverId = ratingDTO.getDriverId();
        Passenger passenger = passengerService.findEntityById(passengerId);

        LOG.info("Save new rating. ID: {}, Driver ID: {}, Passenger ID: {}", ratingId, driverId, passengerId);
        return ratingMapper.toRatingViewingDTO(
                ratingRepository.save(
                        Rating.builder()
                                .id(ratingId)
                                .passenger(passenger)
                                .score(ratingDTO.getScore())
                                .driverId(driverId)
                                .build()
                )
        );
    }

    @Override
    public RatingListDTO getRatingsByPassengerId(UUID passengerId) {
        LOG.debug("Get rating by passenger ID: {}", passengerId);
        return new RatingListDTO(
                ratingRepository.findRatingsByPassengerId(passengerId)
                        .stream()
                        .map(ratingMapper::toRatingViewingDTO)
                        .toList()
        );
    }

    @Override
    public AverageRatingDTO getAveragePassengerRating(UUID passengerId) {
        passengerService.throwExceptionIfPassengerDoesNotExist(passengerId);

        double averageRating = Double.parseDouble(
                new DecimalFormat(DECIMAL_FORMAT_PATTERN)
                        .format(ratingRepository.findRatingsByPassengerId(passengerId)
                                .stream()
                                .mapToDouble(Rating::getScore)
                                .average()
                                .orElse(DEFAULT_SCORE)
                        )
        );
        LOG.debug("Calculate average passenger rating. ID: {}", passengerId);
        return new AverageRatingDTO(averageRating);
    }

}
