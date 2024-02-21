package com.modsen.cabaggregator.driverservice.service.impl;

import com.modsen.cabaggregator.driverservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.driverservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverResponse;
import com.modsen.cabaggregator.driverservice.dto.RatingResponse;
import com.modsen.cabaggregator.driverservice.exception.DriverNotFoundException;
import com.modsen.cabaggregator.driverservice.mapper.RatingMapper;
import com.modsen.cabaggregator.driverservice.model.Driver;
import com.modsen.cabaggregator.driverservice.model.Rating;
import com.modsen.cabaggregator.driverservice.repository.RatingRepository;
import com.modsen.cabaggregator.driverservice.service.DriverService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    private final UUID DRIVER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID PASSENGER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private RatingMapper ratingMapper;

    private Driver driver;
    private CreateRatingRequest request;
    private int expectedScore;

    @BeforeEach
    void setUp() {
        expectedScore = 1;
        driver = Driver.builder()
                .id(DRIVER_ID)
                .build();
        request = CreateRatingRequest.builder()
                .score(expectedScore)
                .passengerId(PASSENGER_ID)
                .build();
    }

    @Test
    void rate_ExistingDriverId_ShouldSaveRatingToRepository() {
        Rating rating = Rating.builder()
                .driver(driver)
                .score(expectedScore)
                .passengerId(PASSENGER_ID)
                .build();
        Mockito.when(driverService.findEntityById(PASSENGER_ID)).thenReturn(driver);
        Mockito.when(ratingRepository.save(Mockito.any())).thenReturn(rating);
        Mockito.when(ratingMapper.toRatingResponse(rating)).thenReturn(
                RatingResponse.builder()
                        .driver(new DriverResponse())
                        .score(expectedScore)
                        .passengerId(PASSENGER_ID)
                        .build()
        );

        RatingResponse actual = ratingService.rate(request, PASSENGER_ID);

        Assertions.assertThat(actual.getScore()).isEqualTo(expectedScore);
        Assertions.assertThat(actual.getPassengerId()).isEqualTo(PASSENGER_ID);
        Mockito.verify(ratingRepository).save(Mockito.any(Rating.class));
        Mockito.verify(ratingMapper).toRatingResponse(Mockito.any(Rating.class));
        Mockito.verify(driverService).findEntityById(PASSENGER_ID);
    }

    @Test
    void rate_NotExistingDriverId_ShouldThrowDriverNotFoundException() {
        Mockito.when(driverService.findEntityById(PASSENGER_ID)).thenThrow(new DriverNotFoundException(DRIVER_ID));

        Assertions.assertThatThrownBy(() ->
                ratingService.rate(request, PASSENGER_ID)
        ).isInstanceOf(DriverNotFoundException.class).hasMessageContaining(
                String.format("Driver with id %s was not found", DRIVER_ID)
        );
        Mockito.verify(ratingRepository, Mockito.never()).save(Mockito.any(Rating.class));
        Mockito.verify(ratingMapper, Mockito.never()).toRatingResponse(Mockito.any(Rating.class));
        Mockito.verify(driverService).findEntityById(PASSENGER_ID);
    }

    @Test
    void getRatingsByDriverId_ShouldReturnAllDriverRatings() {
        final List<Rating> ratings = List.of(
                new Rating(),
                new Rating()
        );
        final List<RatingResponse> expected = List.of(
                new RatingResponse(),
                new RatingResponse()
        );
        Mockito.when(ratingRepository.findRatingsByDriverId(DRIVER_ID)).thenReturn(ratings);
        Mockito.when(ratingMapper.toRatingResponse(ratings.get(0))).thenReturn(expected.get(0));
        Mockito.when(ratingMapper.toRatingResponse(ratings.get(1))).thenReturn(expected.get(1));

        AllRatingsResponse response = ratingService.getRatingsByDriverId(DRIVER_ID);

        Assertions.assertThat(response.getRatings()).containsExactlyElementsOf(expected);
        Mockito.verify(ratingRepository).findRatingsByDriverId(DRIVER_ID);
        Mockito.verify(ratingMapper, Mockito.times(2)).toRatingResponse(Mockito.any(Rating.class));
    }

    @Test
    void getAverageDriverRating() {
        final double expectedAverageRating = 2;
        final List<Rating> ratings = List.of(
                Rating.builder()
                        .id(UUID.fromString("00000000-0000-0000-0000-000000000011"))
                        .driver(driver)
                        .score(1)
                        .passengerId(PASSENGER_ID)
                        .build(),
                Rating.builder()
                        .id(UUID.fromString("00000000-0000-0000-0000-000000000022"))
                        .driver(driver)
                        .score(3)
                        .passengerId(PASSENGER_ID)
                        .build()
        );
        Mockito.when(ratingRepository.findRatingsByDriverId(DRIVER_ID)).thenReturn(ratings);

        AverageRatingResponse response = ratingService.getAverageDriverRating(DRIVER_ID);

        Assertions.assertThat(response.getAverageValue()).isEqualTo(expectedAverageRating);
    }

}
