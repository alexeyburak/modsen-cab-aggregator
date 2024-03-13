package com.modsen.cabaggregator.driverservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.cabaggregator.driverservice.config.DbConfig;
import com.modsen.cabaggregator.driverservice.dto.AllDriversResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.UpdateDriverRequest;
import com.modsen.cabaggregator.driverservice.mapper.DriverMapper;
import com.modsen.cabaggregator.driverservice.model.Driver;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverSortField;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.driverservice.repository.DriverRepository;
import com.modsen.cabaggregator.driverservice.util.Constants;
import com.modsen.cabaggregator.driverservice.util.DriverServiceUtils;
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
class DriverServiceImplIT extends DbConfig {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private ObjectMapper objectMapper;
    private Driver driverFromDb;

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
    }

    @AfterEach
    void setUpAfter() {
        driverRepository.deleteAll();
    }

    @Test
    void findAll_ShouldReturnAllPassengersResponse() throws Exception {
        AllDriversResponse expected = AllDriversResponse.builder()
                .content(List.of(driverMapper.toDriverResponse(driverFromDb)))
                .currentPageNumber(0)
                .totalElements(1)
                .totalPages(1)
                .build();

        mvc.perform(MockMvcRequestBuilders.get(Constants.DRIVERS_ENDPOINT)
                        .requestAttr("page", 1)
                        .requestAttr("size", 10)
                        .requestAttr("sortOrder", Sort.Direction.ASC)
                        .requestAttr("sortField", DriverSortField.NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void save_ValidUser_ShouldSaveUserToRepository() throws Exception {
        String phone = "80299998800";
        CreateDriverRequest request = CreateDriverRequest.builder()
                .name("sname")
                .surname("ssurname")
                .phone(phone)
                .build();

        mvc.perform(MockMvcRequestBuilders.post(Constants.DRIVERS_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Assertions.assertThat(driverRepository.existsByPhone(phone)).isTrue();
    }

    @Test
    void save_NotUniqueUserPhone_ShouldThrowPhoneIsAlreadyExistsException() throws Exception {
        String phone = driverFromDb.getPhone();
        CreateDriverRequest request = CreateDriverRequest.builder()
                .name("name")
                .surname("surname")
                .phone(phone)
                .build();

        mvc.perform(MockMvcRequestBuilders.post(Constants.DRIVERS_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Assertions.assertThat(driverRepository.existsByPhone(phone)).isTrue();
    }

    @Test
    void delete_ShouldDeleteDriverFromRepository() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(
                                Constants.DRIVERS_ENDPOINT + Constants.ID_MAPPING, driverFromDb.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertThat(driverRepository.existsByPhone(driverFromDb.getPhone())).isFalse();
    }

    @Test
    void findById_DriverExists_ShouldReturnDriverResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(
                                Constants.DRIVERS_ENDPOINT + Constants.ID_MAPPING, driverFromDb.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(driverMapper.toDriverResponse(driverFromDb)))
                );
    }

    @Test
    void findById_PassengerNotExists_ShouldThrowPassengerNotFoundException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(
                                Constants.DRIVERS_ENDPOINT + Constants.ID_MAPPING, UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void update_ValidUser_ShouldUpdateUserAndSaveInRepository() throws Exception {
        final UpdateDriverRequest request = UpdateDriverRequest.builder()
                .name("newName")
                .surname("newSurname")
                .phone("80290000110")
                .status(DriverStatus.UNAVAILABLE)
                .build();

        mvc.perform(MockMvcRequestBuilders.put(
                                Constants.DRIVERS_ENDPOINT + Constants.ID_MAPPING, driverFromDb.getId().toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Driver actual = driverRepository.findById(driverFromDb.getId()).get();
        Assertions.assertThat(actual.getName()).isEqualTo(request.getName());
        Assertions.assertThat(actual.getSurname()).isEqualTo(request.getSurname());
        Assertions.assertThat(actual.getPhone()).isEqualTo(request.getPhone());
        Assertions.assertThat(actual.getStatus()).isEqualTo(request.getStatus());
    }

}
