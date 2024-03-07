package com.modsen.cabaggregator.passengerservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.cabaggregator.passengerservice.config.DbConfig;
import com.modsen.cabaggregator.passengerservice.dto.AllPassengersResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.mapper.PassengerMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.model.enumeration.PassengerSortField;
import com.modsen.cabaggregator.passengerservice.repository.PassengerRepository;
import com.modsen.cabaggregator.passengerservice.util.Constants;
import com.modsen.cabaggregator.passengerservice.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PassengerServiceImplIT extends DbConfig {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private PassengerMapper passengerMapper;
    @Autowired
    private ObjectMapper objectMapper;
    private Passenger passengerFromDb;

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
    }

    @AfterEach
    void setUpAfter() {
        passengerRepository.deleteAll();
    }

    @Test
    void findAll_ShouldReturnAllPassengersResponse() throws Exception {
        AllPassengersResponse expected = AllPassengersResponse.builder()
                .content(List.of(passengerMapper.toPassengerResponse(passengerFromDb)))
                .currentPageNumber(0)
                .totalElements(1)
                .totalPages(1)
                .build();

        mvc.perform(MockMvcRequestBuilders.get(Constants.PASSENGERS_ENDPOINT)
                        .requestAttr("page", 1)
                        .requestAttr("size", 10)
                        .requestAttr("sortOrder", Sort.Direction.ASC)
                        .requestAttr("sortField", PassengerSortField.NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void save_ValidUser_ShouldSaveUserToRepository() throws Exception {
        String email = "email@email.com";
        CreatePassengerRequest request = CreatePassengerRequest.builder()
                .name("sname")
                .surname("ssurname")
                .email(email)
                .phone("80290000001")
                .build();

        mvc.perform(MockMvcRequestBuilders.post(Constants.PASSENGERS_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Assertions.assertThat(passengerRepository.existsByEmail(email)).isTrue();
    }

    @Test
    void save_NotUniqueUserMail_ShouldThrowEmailIsAlreadyExistsException() throws Exception {
        String email = passengerFromDb.getEmail();
        CreatePassengerRequest request = CreatePassengerRequest.builder()
                .name("name")
                .surname("surname")
                .email(email)
                .phone("80290000000")
                .build();

        mvc.perform(MockMvcRequestBuilders.post(Constants.PASSENGERS_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Assertions.assertThat(passengerRepository.existsByEmail(email)).isTrue();
    }

    @Test
    void save_NotUniqueUserPhone_ShouldThrowPhoneIsAlreadyExistsException() throws Exception {
        String phone = passengerFromDb.getPhone();
        CreatePassengerRequest request = CreatePassengerRequest.builder()
                .name("name")
                .surname("surname")
                .email("test@email.com")
                .phone(phone)
                .build();

        mvc.perform(MockMvcRequestBuilders.post(Constants.PASSENGERS_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Assertions.assertThat(passengerRepository.existsByPhone(phone)).isTrue();
    }

    @Test
    void delete_ShouldDeletePassengerFromRepository() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(
                                Constants.PASSENGERS_ENDPOINT + Constants.ID_MAPPING, passengerFromDb.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertThat(passengerRepository.existsByPhone(passengerFromDb.getPhone())).isFalse();
    }

    @Test
    void findById_PassengerExists_ShouldReturnPassengerResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(
                                Constants.PASSENGERS_ENDPOINT + Constants.ID_MAPPING, passengerFromDb.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(passengerMapper.toPassengerResponse(passengerFromDb)))
                );
    }

    @Test
    void findById_PassengerNotExists_ShouldThrowPassengerNotFoundException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(
                                Constants.PASSENGERS_ENDPOINT + Constants.ID_MAPPING, UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void update_ValidUser_ShouldUpdateUserAndSaveInRepository() throws Exception {
        final UpdatePassengerRequest request = UpdatePassengerRequest.builder()
                .name("newName")
                .surname("newSurname")
                .email("email.email@email.com")
                .phone("80290000110")
                .build();

        mvc.perform(MockMvcRequestBuilders.post(
                                Constants.PASSENGERS_ENDPOINT + Constants.ID_MAPPING, passengerFromDb.getId().toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(passengerMapper.toPassengerResponse(passengerFromDb)))
                );
    }

}
