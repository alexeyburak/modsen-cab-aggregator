package com.modsen.cabaggregator.passengerservice.service;

import com.modsen.cabaggregator.passengerservice.dto.AllPassengersResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.PassengerResponse;
import com.modsen.cabaggregator.passengerservice.exception.PassengerNotFoundException;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.UUID;

public interface PassengerService {
    AllPassengersResponse findAll(Integer page, Integer size, PassengerSortCriteria sort);

    PassengerResponse save(OAuth2User principal);

    void delete(UUID id);

    PassengerResponse findById(UUID id) throws PassengerNotFoundException;

    PassengerResponse update(UUID id, UpdatePassengerRequest updatePassengerRequest) throws PassengerNotFoundException;

    Passenger findEntityById(UUID id) throws PassengerNotFoundException;

    void throwExceptionIfPassengerDoesNotExist(UUID passengerId) throws PassengerNotFoundException;
}
