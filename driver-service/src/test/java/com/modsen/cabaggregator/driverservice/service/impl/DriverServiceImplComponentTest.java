package com.modsen.cabaggregator.driverservice.service.impl;

import com.modsen.cabaggregator.driverservice.dto.AllDriversResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverResponse;
import com.modsen.cabaggregator.driverservice.dto.DriverSortCriteria;
import com.modsen.cabaggregator.driverservice.dto.UpdateDriverRequest;
import com.modsen.cabaggregator.driverservice.mapper.DriverMapper;
import com.modsen.cabaggregator.driverservice.model.Driver;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverSortField;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.driverservice.repository.DriverRepository;
import com.modsen.cabaggregator.driverservice.util.DriverServiceUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.assertj.core.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@CucumberContextConfiguration
public class DriverServiceImplComponentTest {

    @InjectMocks
    private DriverServiceImpl driverService;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    private AllDriversResponse allDriversResponse;
    private CreateDriverRequest createDriverRequest;
    private UpdateDriverRequest updateDriverRequest;
    private DriverResponse driverResponse;
    private UUID driverId;

    @Given("the driver repository contains the following drivers:")
    public void theDriverRepositoryContainsTheFollowingDrivers(List<Map<String, String>> driversData) {
        List<Driver> drivers = new ArrayList<>();
        for (Map<String, String> driverData : driversData) {
            Driver driver = new Driver();
            driver.setName(driverData.get("name"));
            drivers.add(driver);
        }
        Mockito.when(driverRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, DriverSortField.NAME.getFieldName())))
        ).thenReturn(new PageImpl<>(drivers));
    }

    @When("I request all drivers with page {int}, size {int}, and ascending order by name")
    public void iRequestAllDriversWithPageAndSize(Integer page, Integer size) {
        DriverSortCriteria sortCriteria = new DriverSortCriteria(DriverSortField.NAME, Sort.Direction.ASC);
        allDriversResponse = driverService.findAll(page, size, sortCriteria);
    }

    @Then("the response should contain {int} drivers")
    public void theResponseShouldContainPassengers(int expectedCount) {
        Assertions.assertThat(allDriversResponse.getContent()).hasSize(expectedCount);
    }

    @Given("I have a new driver request with name {string}, surname {string}, phone {string}")
    public void createDriverRequest(String name, String surname, String phone) {
        final Driver driver = DriverServiceUtils.buildDriver();
        createDriverRequest = DriverServiceUtils.buildCreateDriverRequest(name, surname, phone);
        Mockito.when(driverRepository.save(Mockito.any(Driver.class))).thenReturn(driver);
        Mockito.when(driverMapper.toDriverResponse(driver)).thenReturn(
                DriverServiceUtils.buildDriverResponse(driver.getId(), name, surname, DriverStatus.AVAILABLE, phone)
        );
    }

    @When("I save the driver")
    public void saveDriver() {
        driverResponse = driverService.save(createDriverRequest);
    }

    @Then("the driver should be saved successfully")
    public void verifyPassengerSaved() {
        assertNotNull(driverResponse);
    }

    @Then("the driver details should match the request details")
    public void verifyCreatePassengerDetails() {
        assertEquals(createDriverRequest.getName(), driverResponse.getName());
        assertEquals(createDriverRequest.getSurname(), driverResponse.getSurname());
        assertEquals(createDriverRequest.getPhone(), driverResponse.getPhone());
    }

    @Given("there is a driver with ID {string}")
    public void createPassengerWithId(String id) {
        driverId = UUID.fromString(id);
        Mockito.when(driverRepository.existsById(UUID.fromString(id))).thenReturn(false);
    }

    @When("I delete the driver")
    public void deletePassenger() {
        driverService.delete(driverId);
    }

    @Then("the driver with ID {string} should be deleted")
    public void verifyPassengerDeleted(String id) {
        assertFalse(driverRepository.existsById(UUID.fromString(id)));
    }

    @When("I search for the driver with ID {string}")
    public void findPassengerById(String id) {
        final Driver driver = DriverServiceUtils.buildDriver();
        final DriverResponse expected = new DriverResponse();
        Mockito.when(driverRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(driver));
        Mockito.when(driverMapper.toDriverResponse(driver)).thenReturn(expected);
        driverResponse = driverService.findById(UUID.fromString(id));
    }

    @Then("I should receive the driver details")
    public void verifyPassengerFound() {
        assertNotNull(driverResponse);
    }

    @And("I have an update request with name {string}, surname {string}, phone {string}")
    public void createUpdateRequest(String name, String surname, String phone) {
        Driver driver = DriverServiceUtils.buildDriver();
        updateDriverRequest = DriverServiceUtils.buildUpdateDriverRequest(name, surname, DriverStatus.AVAILABLE, phone);
        Mockito.when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));
        Mockito.when(driverRepository.save(Mockito.any(Driver.class))).thenReturn(driver);
        Mockito.when(driverMapper.toDriverResponse(driver)).thenReturn(
                DriverServiceUtils.buildDriverResponse(driver.getId(), name, surname, DriverStatus.AVAILABLE, phone)
        );
    }

    @When("I update the driver with ID {string}")
    public void updatePassenger(String id) {
        driverResponse = driverService.update(UUID.fromString(id), updateDriverRequest);
    }

    @Then("the driver details should be updated successfully")
    public void verifyPassengerUpdated() {
        assertNotNull(driverResponse);
    }

    @Then("the updated driver details should match the update request")
    public void verifyUpdatePassengerDetails() {
        assertEquals(updateDriverRequest.getName(), driverResponse.getName());
        assertEquals(updateDriverRequest.getSurname(), driverResponse.getSurname());
        assertEquals(updateDriverRequest.getPhone(), driverResponse.getPhone());
    }

}
