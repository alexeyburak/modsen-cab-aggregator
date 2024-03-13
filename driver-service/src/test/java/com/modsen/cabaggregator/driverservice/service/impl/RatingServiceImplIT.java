package com.modsen.cabaggregator.driverservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.cabaggregator.driverservice.config.DbConfig;
import com.modsen.cabaggregator.driverservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.driverservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.driverservice.mapper.RatingMapper;
import com.modsen.cabaggregator.driverservice.model.Driver;
import com.modsen.cabaggregator.driverservice.model.Rating;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.driverservice.repository.DriverRepository;
import com.modsen.cabaggregator.driverservice.repository.RatingRepository;
import com.modsen.cabaggregator.driverservice.util.Constants;
import com.modsen.cabaggregator.driverservice.util.DriverServiceUtils;
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
    private DriverRepository driverRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private RatingMapper ratingMapper;
    @Autowired
    private ObjectMapper objectMapper;
    private Driver driverFromDb;
    private Rating ratingFromDb;

    @BeforeEach
    void setUpBefore() {
        driverFromDb = Driver.builder()
                .name(DriverServiceUtils.NAME)
                .surname(DriverServiceUtils.SURNAME)
                .status(DriverStatus.AVAILABLE)
                .phone(DriverServiceUtils.PHONE)
                .active(true)
                .build();
        driverRepository.save(driverFromDb);
        ratingFromDb = Rating.builder()
                .driver(driverFromDb)
                .passengerId(DriverServiceUtils.PASSENGER_ID)
                .score(5)
                .build();
        ratingRepository.save(ratingFromDb);
    }

    @AfterEach
    void setUpAfter() {
        ratingRepository.deleteAll();
        driverRepository.deleteAll();
    }

    @Test
    void rate_ExistingPassengerId_ShouldSaveRatingToRepository() throws Exception {
        CreateRatingRequest request = DriverServiceUtils.buildCreateRatingRequest();

        mvc.perform(MockMvcRequestBuilders.post(Constants.RATINGS_ENDPOINT, driverFromDb.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertThat(ratingRepository.findAll()).hasSize(2);
    }

    @Test
    void rate_NotExistingPassengerId_ShouldThrowPassengerNotFoundException() throws Exception {
        CreateRatingRequest request = DriverServiceUtils.buildCreateRatingRequest();

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

        mvc.perform(MockMvcRequestBuilders.get(Constants.RATINGS_ENDPOINT, driverFromDb.getId())
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

        mvc.perform(MockMvcRequestBuilders.get(Constants.RATINGS_ENDPOINT + Constants.AVERAGE_MAPPING, driverFromDb.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(expected)
                ));
    }
}
