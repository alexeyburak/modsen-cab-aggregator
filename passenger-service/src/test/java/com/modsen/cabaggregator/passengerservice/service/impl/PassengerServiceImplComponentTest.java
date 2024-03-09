package com.modsen.cabaggregator.passengerservice.service.impl;

import com.modsen.cabaggregator.passengerservice.client.PaymentServiceClient;
import com.modsen.cabaggregator.passengerservice.dto.AllPassengersResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.PassengerResponse;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.mapper.PassengerMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.model.enumeration.PassengerSortField;
import com.modsen.cabaggregator.passengerservice.repository.PassengerRepository;
import com.modsen.cabaggregator.passengerservice.util.TestUtils;
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
public class PassengerServiceImplComponentTest {

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private PaymentServiceClient paymentClient;

    private AllPassengersResponse allPassengersResponse;
    private CreatePassengerRequest createPassengerRequest;
    private UpdatePassengerRequest updatePassengerRequest;
    private PassengerResponse passengerResponse;
    private UUID passengerId;

    @Given("the passenger repository contains the following passengers:")
    public void thePassengerRepositoryContainsTheFollowingPassengers(List<Map<String, String>> passengersData) {
        List<Passenger> passengers = new ArrayList<>();
        for (Map<String, String> passengerData : passengersData) {
            Passenger passenger = new Passenger();
            passenger.setName(passengerData.get("name"));
            passengers.add(passenger);
        }
        Mockito.when(passengerRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, PassengerSortField.EMAIL.getFiledName())))
        ).thenReturn(new PageImpl<>(passengers));
    }

    @When("I request all passengers with page {int}, size {int}, and ascending order by email")
    public void iRequestAllPassengersWithPageAndSize(Integer page, Integer size) {
        PassengerSortCriteria sortCriteria = new PassengerSortCriteria(PassengerSortField.EMAIL, Sort.Direction.ASC);
        allPassengersResponse = passengerService.findAll(page, size, sortCriteria);
    }

    @Then("the response should contain {int} passengers")
    public void theResponseShouldContainPassengers(int expectedCount) {
        Assertions.assertThat(allPassengersResponse.getContent()).hasSize(expectedCount);
    }

    @Given("I have a new passenger request with name {string}, surname {string}, email {string}, phone {string}")
    public void createPassengerRequest(String name, String surname, String email, String phone) {
        final Passenger passenger = TestUtils.buildPassenger();
        createPassengerRequest = TestUtils.buildCreatePassengerRequest(name, surname, email, phone);
        Mockito.when(passengerRepository.save(Mockito.any(Passenger.class))).thenReturn(passenger);
        Mockito.when(passengerMapper.toPassengerResponse(passenger)).thenReturn(
                TestUtils.buildPassengerResponse(passenger.getId(), name, surname, email, phone)
        );
    }

    @When("I save the passenger")
    public void savePassenger() {
        passengerResponse = passengerService.save(createPassengerRequest);
    }

    @Then("the passenger should be saved successfully")
    public void verifyPassengerSaved() {
        assertNotNull(passengerResponse);
    }

    @Then("the passenger details should match the request details")
    public void verifyCreatePassengerDetails() {
        assertEquals(createPassengerRequest.getName(), passengerResponse.getName());
        assertEquals(createPassengerRequest.getEmail(), passengerResponse.getEmail());
        assertEquals(createPassengerRequest.getSurname(), passengerResponse.getSurname());
        assertEquals(createPassengerRequest.getPhone(), passengerResponse.getPhone());
    }

    @Given("there is a passenger with ID {string}")
    public void createPassengerWithId(String id) {
        passengerId = UUID.fromString(id);
        Mockito.when(passengerRepository.existsById(UUID.fromString(id))).thenReturn(false);
    }

    @When("I delete the passenger")
    public void deletePassenger() {
        passengerService.delete(passengerId);
    }

    @Then("the passenger with ID {string} should be deleted")
    public void verifyPassengerDeleted(String id) {
        assertFalse(passengerRepository.existsById(UUID.fromString(id)));
    }

    @When("I search for the passenger with ID {string}")
    public void findPassengerById(String id) {
        final Passenger passenger = TestUtils.buildPassenger();
        final PassengerResponse expected = new PassengerResponse();
        Mockito.when(passengerRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(passenger));
        Mockito.when(passengerMapper.toPassengerResponse(passenger)).thenReturn(expected);
        passengerResponse = passengerService.findById(UUID.fromString(id));
    }

    @Then("I should receive the passenger details")
    public void verifyPassengerFound() {
        assertNotNull(passengerResponse);
    }

    @And("I have an update request with name {string}, surname {string}, email {string}, phone {string}")
    public void createUpdateRequest(String name, String surname, String email, String phone) {
        Passenger passenger = TestUtils.buildPassenger();
        updatePassengerRequest = TestUtils.buildUpdatePassengerRequest(name, surname, email, phone);
        Mockito.when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        Mockito.when(passengerRepository.save(Mockito.any(Passenger.class))).thenReturn(passenger);
        Mockito.when(passengerMapper.toPassengerResponse(passenger)).thenReturn(
                TestUtils.buildPassengerResponse(passenger.getId(), name, surname, email, phone)
        );
    }

    @When("I update the passenger with ID {string}")
    public void updatePassenger(String id) {
        passengerResponse = passengerService.update(UUID.fromString(id), updatePassengerRequest);
    }

    @Then("the passenger details should be updated successfully")
    public void verifyPassengerUpdated() {
        assertNotNull(passengerResponse);
    }

    @Then("the updated passenger details should match the update request")
    public void verifyUpdatePassengerDetails() {
        assertEquals(updatePassengerRequest.getName(), passengerResponse.getName());
        assertEquals(updatePassengerRequest.getEmail(), passengerResponse.getEmail());
        assertEquals(updatePassengerRequest.getSurname(), passengerResponse.getSurname());
        assertEquals(updatePassengerRequest.getPhone(), passengerResponse.getPhone());
    }

}
