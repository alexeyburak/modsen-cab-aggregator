package com.modsen.cabaggregator.passengerservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.cabaggregator.passengerservice.config.DbConfig;
import com.modsen.cabaggregator.passengerservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.passengerservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.passengerservice.mapper.RatingMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.model.Rating;
import com.modsen.cabaggregator.passengerservice.repository.PassengerRepository;
import com.modsen.cabaggregator.passengerservice.repository.RatingRepository;
import com.modsen.cabaggregator.passengerservice.util.Constants;
import com.modsen.cabaggregator.passengerservice.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RatingServiceImplIT extends DbConfig {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private RatingMapper ratingMapper;
    @Autowired
    private ObjectMapper objectMapper;
    private Passenger passengerFromDb;
    private Rating ratingFromDb;

    @BeforeEach
    void setUpBefore() {
        passengerFromDb = Passenger.builder()
                .name(TestUtils.NAME)
                .surname(TestUtils.SURNAME)
                .email(TestUtils.EMAIL)
                .phone(TestUtils.PHONE)
                .active(true)
                .build();
        passengerRepository.save(passengerFromDb);
        ratingFromDb = Rating.builder()
                .passenger(passengerFromDb)
                .driverId(TestUtils.DRIVER_ID)
                .score(5)
                .build();
        ratingRepository.save(ratingFromDb);
    }

    @AfterEach
    void setUpAfter() {
        ratingRepository.deleteAll();
        passengerRepository.deleteAll();
    }

    @Test
    void rate_ExistingPassengerId_ShouldSaveRatingToRepository() throws Exception {
        CreateRatingRequest request = TestUtils.buildCreateRatingRequest();

        mvc.perform(MockMvcRequestBuilders.post(Constants.RATINGS_ENDPOINT, passengerFromDb.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertThat(ratingRepository.findAll()).hasSize(2);
    }

    @Test
    void rate_NotExistingPassengerId_ShouldThrowPassengerNotFoundException() throws Exception {
        CreateRatingRequest request = TestUtils.buildCreateRatingRequest();

        mvc.perform(MockMvcRequestBuilders.post(Constants.RATINGS_ENDPOINT, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Assertions.assertThat(ratingRepository.findAll()).hasSize(1);
    }

    @Test
    void getRatingsByPassengerId_ShouldReturnAllPassengerRatings() throws Exception {
        AllRatingsResponse expected = AllRatingsResponse.builder()
                .ratings(List.of(ratingMapper.toRatingResponse(ratingFromDb)))
                .build();

        mvc.perform(MockMvcRequestBuilders.get(Constants.RATINGS_ENDPOINT, passengerFromDb.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(expected)
                ));

        Assertions.assertThat(ratingRepository.findAll()).hasSize(1);
    }

    @Test
    void getAveragePassengerRating() throws Exception {
        AverageRatingResponse expected = AverageRatingResponse.builder()
                .averageValue(5.0)
                .build();

        mvc.perform(MockMvcRequestBuilders.get(Constants.RATINGS_ENDPOINT + Constants.AVERAGE_MAPPING, passengerFromDb.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(expected)
                ));
    }
}
