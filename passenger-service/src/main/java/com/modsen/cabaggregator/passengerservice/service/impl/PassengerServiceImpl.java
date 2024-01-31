package com.modsen.cabaggregator.passengerservice.service.impl;

import com.modsen.cabaggregator.passengerservice.dto.AllPassengersResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.PassengerResponse;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.exception.EmailIsAlreadyExistsException;
import com.modsen.cabaggregator.passengerservice.exception.InvalidPageRequestException;
import com.modsen.cabaggregator.passengerservice.exception.PassengerNotFoundException;
import com.modsen.cabaggregator.passengerservice.exception.PhoneIsAlreadyExistsException;
import com.modsen.cabaggregator.passengerservice.mapper.PassengerMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.repository.PassengerRepository;
import com.modsen.cabaggregator.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    public static final String PASSENGER_WAS_NOT_FOUND = "Passenger with %s was not found";

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public AllPassengersResponse findAll(Integer page, Integer size, PassengerSortCriteria sort) {
        validatePageRequestParameters(page, size);

        Sort sortBy = Sort.by(sort.getOrder(), sort.getField().getFiledName());
        Page<Passenger> passengers = passengerRepository.findAll(PageRequest.of(page, size, sortBy));
        return new AllPassengersResponse(
                passengers.getContent()
                        .stream()
                        .map(passengerMapper::toPassengerResponse)
                        .toList(),
                page,
                passengers.getTotalPages(),
                passengers.getTotalElements()
        );
    }

    @Override
    @Transactional
    public PassengerResponse save(CreatePassengerRequest request) {
        validateUniqueData(request);

        final String email = request.getEmail().toLowerCase();
        log.info("Save new passenger. Email: {}", email);
        return passengerMapper.toPassengerResponse(
                passengerRepository.save(
                        Passenger.builder()
                                .name(request.getName())
                                .surname(request.getSurname())
                                .email(email)
                                .phone(request.getPhone())
                                .active(true)
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        passengerRepository.deleteById(id);
        log.info("Delete passenger. ID: {}", id);
    }

    @Override
    public PassengerResponse findById(UUID id) {
        return passengerMapper.toPassengerResponse(
                findEntityById(id)
        );
    }

    @Override
    @Transactional
    public PassengerResponse update(UUID id, UpdatePassengerRequest request) {
        Passenger passenger = findEntityById(id);

        passenger.setName(request.getName());
        passenger.setSurname(request.getSurname());
        updatePhone(request.getPhone(), passenger);
        updateEmail(request.getEmail(), passenger);

        log.info("Update passenger. ID: {}", id);
        return passengerMapper.toPassengerResponse(
                passengerRepository.save(passenger)
        );
    }

    @Override
    public Passenger findEntityById(UUID id) throws PassengerNotFoundException {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(String.format(PASSENGER_WAS_NOT_FOUND, id)));
    }

    @Override
    public void throwExceptionIfPassengerDoesNotExist(UUID passengerId) throws PassengerNotFoundException {
        if (!passengerRepository.existsById(passengerId)) {
            throw new PassengerNotFoundException(String.format(PASSENGER_WAS_NOT_FOUND, passengerId));
        }
    }

    private void validatePageRequestParameters(Integer page, Integer size) {
        if (page < 0 || size < 0) {
            throw new InvalidPageRequestException();
        }
    }

    private void updatePhone(String updatedPhone, Passenger passenger) {
        if (!Objects.equals(passenger.getPhone(), updatedPhone)) {
            throwExceptionIfPhoneExists(updatedPhone);
            passenger.setPhone(updatedPhone);
        }
    }

    private void throwExceptionIfPhoneExists(String phone) {
        if (passengerRepository.existsByPhone(phone)) {
            throw new PhoneIsAlreadyExistsException(phone);
        }
    }

    private void updateEmail(String updatedEmail, Passenger passenger) {
        updatedEmail = updatedEmail.toLowerCase(Locale.ROOT);
        if (!Objects.equals(passenger.getEmail(), updatedEmail)) {
            throwExceptionIfEmailExists(updatedEmail);
            passenger.setEmail(updatedEmail);
        }
    }

    private void throwExceptionIfEmailExists(String email) {
        if (passengerRepository.existsByEmail(email)) {
            throw new EmailIsAlreadyExistsException(email);
        }
    }

    private void validateUniqueData(CreatePassengerRequest request) {
        throwExceptionIfEmailExists(request.getEmail());
        throwExceptionIfPhoneExists(request.getPhone());
    }

}
