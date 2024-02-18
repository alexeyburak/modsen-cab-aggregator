package com.modsen.cabaggregator.passengerservice.service.impl;

import com.modsen.cabaggregator.passengerservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.passengerservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.passengerservice.dto.PassengerResponse;
import com.modsen.cabaggregator.passengerservice.dto.RatingResponse;
import com.modsen.cabaggregator.passengerservice.exception.PassengerNotFoundException;
import com.modsen.cabaggregator.passengerservice.mapper.RatingMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.model.Rating;
import com.modsen.cabaggregator.passengerservice.repository.RatingRepository;
import com.modsen.cabaggregator.passengerservice.service.PassengerService;
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

    private final UUID PASSENGER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final UUID DRIVER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private PassengerService passengerService;

    @Mock
    private RatingMapper ratingMapper;

    private Passenger passenger;
    private CreateRatingRequest request;
    private int expectedScore;

    @BeforeEach
    void setUp() {
        expectedScore = 1;
        passenger = Passenger.builder()
                .id(PASSENGER_ID)
                .build();
        request = CreateRatingRequest.builder()
                .score(expectedScore)
                .driverId(DRIVER_ID)
                .build();
    }

    @Test
    void rate_ExistingPassengerId_ShouldSaveRatingToRepository() {
        Rating rating = Rating.builder()
                .passenger(passenger)
                .score(expectedScore)
                .driverId(DRIVER_ID)
                .build();
        Mockito.when(passengerService.findEntityById(PASSENGER_ID)).thenReturn(passenger);
        Mockito.when(ratingRepository.save(Mockito.any())).thenReturn(rating);
        Mockito.when(ratingMapper.toRatingResponse(rating)).thenReturn(
                RatingResponse.builder()
                        .passenger(new PassengerResponse())
                        .score(expectedScore)
                        .driverId(DRIVER_ID)
                        .build()
        );

        RatingResponse actual = ratingService.rate(request, PASSENGER_ID);

        Assertions.assertThat(actual.getScore()).isEqualTo(expectedScore);
        Assertions.assertThat(actual.getDriverId()).isEqualTo(DRIVER_ID);
        Mockito.verify(ratingRepository).save(Mockito.any(Rating.class));
        Mockito.verify(ratingMapper).toRatingResponse(Mockito.any(Rating.class));
        Mockito.verify(passengerService).findEntityById(PASSENGER_ID);
    }

    @Test
    void rate_NotExistingPassengerId_ShouldThrowPassengerNotFoundException() {
        final String message = "Exception message";
        Mockito.when(passengerService.findEntityById(PASSENGER_ID)).thenThrow(new PassengerNotFoundException(message));

        Assertions.assertThatThrownBy(() ->
                ratingService.rate(request, PASSENGER_ID)
        ).isInstanceOf(PassengerNotFoundException.class).hasMessageContaining(message);
        Mockito.verify(ratingRepository, Mockito.never()).save(Mockito.any(Rating.class));
        Mockito.verify(ratingMapper, Mockito.never()).toRatingResponse(Mockito.any(Rating.class));
        Mockito.verify(passengerService).findEntityById(PASSENGER_ID);
    }

    @Test
    void getRatingsByPassengerId_ShouldReturnAllPassengerRatings() {
        final List<Rating> ratings = List.of(
                new Rating(),
                new Rating()
        );
        final List<RatingResponse> expected = List.of(
                new RatingResponse(),
                new RatingResponse()
        );
        Mockito.when(ratingRepository.findRatingsByPassengerId(PASSENGER_ID)).thenReturn(ratings);
        Mockito.when(ratingMapper.toRatingResponse(ratings.get(0))).thenReturn(expected.get(0));
        Mockito.when(ratingMapper.toRatingResponse(ratings.get(1))).thenReturn(expected.get(1));

        AllRatingsResponse response = ratingService.getRatingsByPassengerId(PASSENGER_ID);

        Assertions.assertThat(response.getRatings()).containsExactlyElementsOf(expected);
        Mockito.verify(ratingRepository).findRatingsByPassengerId(PASSENGER_ID);
        Mockito.verify(ratingMapper, Mockito.times(2)).toRatingResponse(Mockito.any(Rating.class));
    }

    @Test
    void getAveragePassengerRating() {
        final double expectedAverageRating = 2;
        final List<Rating> ratings = List.of(
                Rating.builder()
                        .id(UUID.fromString("00000000-0000-0000-0000-000000000011"))
                        .passenger(passenger)
                        .score(1)
                        .driverId(DRIVER_ID)
                        .build(),
                Rating.builder()
                        .id(UUID.fromString("00000000-0000-0000-0000-000000000022"))
                        .passenger(passenger)
                        .score(3)
                        .driverId(DRIVER_ID)
                        .build()
        );
        Mockito.when(ratingRepository.findRatingsByPassengerId(PASSENGER_ID)).thenReturn(ratings);

        AverageRatingResponse response = ratingService.getAveragePassengerRating(PASSENGER_ID);

        Assertions.assertThat(response.getAverageValue()).isEqualTo(expectedAverageRating);
    }

}
