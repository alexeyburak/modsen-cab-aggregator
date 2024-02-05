package com.modsen.cabaggregator.passengerservice.service.impl;

import com.modsen.cabaggregator.common.util.GlobalConstants;
import com.modsen.cabaggregator.passengerservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.passengerservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.passengerservice.dto.RatingResponse;
import com.modsen.cabaggregator.passengerservice.mapper.RatingMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.model.Rating;
import com.modsen.cabaggregator.passengerservice.repository.RatingRepository;
import com.modsen.cabaggregator.passengerservice.service.PassengerService;
import com.modsen.cabaggregator.passengerservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final PassengerService passengerService;
    private final RatingMapper ratingMapper;

    @Override
    @Transactional
    public RatingResponse rate(CreateRatingRequest request, UUID passengerId) {
        final UUID driverId = request.getDriverId();
        Passenger passenger = passengerService.findEntityById(passengerId);

        log.info("Save new rating. Driver ID: {}, Passenger ID: {}", driverId, passengerId);
        return ratingMapper.toRatingResponse(
                ratingRepository.save(
                        Rating.builder()
                                .passenger(passenger)
                                .score(request.getScore())
                                .driverId(driverId)
                                .build()
                )
        );
    }

    @Override
    public AllRatingsResponse getRatingsByPassengerId(UUID passengerId) {
        log.debug("Get rating by passenger ID: {}", passengerId);
        return new AllRatingsResponse(
                ratingRepository.findRatingsByPassengerId(passengerId)
                        .stream()
                        .map(ratingMapper::toRatingResponse)
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AverageRatingResponse getAveragePassengerRating(UUID passengerId) {
        passengerService.throwExceptionIfPassengerDoesNotExist(passengerId);

        double averageRating = Double.parseDouble(
                new DecimalFormat(GlobalConstants.DECIMAL_FORMAT_PATTERN)
                        .format(ratingRepository.findRatingsByPassengerId(passengerId)
                                .stream()
                                .mapToDouble(Rating::getScore)
                                .average()
                                .orElse(GlobalConstants.DEFAULT_SCORE)
                        )
        );
        log.debug("Calculate average passenger rating. ID: {}", passengerId);
        return new AverageRatingResponse(averageRating);
    }

}
