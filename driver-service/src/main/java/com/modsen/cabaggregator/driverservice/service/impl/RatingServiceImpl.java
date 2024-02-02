package com.modsen.cabaggregator.driverservice.service.impl;

import com.modsen.cabaggregator.common.util.GlobalConstants;
import com.modsen.cabaggregator.driverservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.driverservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.driverservice.dto.RatingResponse;
import com.modsen.cabaggregator.driverservice.mapper.RatingMapper;
import com.modsen.cabaggregator.driverservice.model.Driver;
import com.modsen.cabaggregator.driverservice.model.Rating;
import com.modsen.cabaggregator.driverservice.repository.RatingRepository;
import com.modsen.cabaggregator.driverservice.service.DriverService;
import com.modsen.cabaggregator.driverservice.service.RatingService;
import com.modsen.cabaggregator.driverservice.util.Constants;
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
    private final DriverService driverService;
    private final RatingMapper ratingMapper;

    @Override
    @Transactional
    public RatingResponse rate(CreateRatingRequest createRatingRequest, UUID driverId) {
        final UUID passengerId = createRatingRequest.getPassengerId();
        Driver driver = driverService.findEntityById(driverId);

        log.info("Save new rating. Driver ID: {}, Passenger ID: {}", driverId, passengerId);
        return ratingMapper.toRatingResponse(
                ratingRepository.save(
                        Rating.builder()
                                .driver(driver)
                                .score(createRatingRequest.getScore())
                                .passengerId(passengerId)
                                .build()
                )
        );
    }

    @Override
    public AllRatingsResponse getRatingsByDriverId(UUID driverId) {
        log.debug("Get rating by driver ID: {}", driverId);
        return new AllRatingsResponse(
                ratingRepository.findRatingsByDriverId(driverId)
                        .stream()
                        .map(ratingMapper::toRatingResponse)
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AverageRatingResponse getAverageDriverRating(UUID driverId) {
        driverService.throwExceptionIfDriverDoesNotExist(driverId);

        double averageRating = Double.parseDouble(
                new DecimalFormat(GlobalConstants.DECIMAL_FORMAT_PATTERN)
                        .format(ratingRepository.findRatingsByDriverId(driverId)
                                .stream()
                                .mapToDouble(Rating::getScore)
                                .average()
                                .orElse(GlobalConstants.DEFAULT_SCORE)
                        )
        );
        log.debug("Calculate average driver rating. ID: {}", driverId);
        return new AverageRatingResponse(averageRating);
    }

}
