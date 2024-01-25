package com.modsen.cabaggregator.passengerservice.service.impl;

import com.modsen.cabaggregator.passengerservice.dto.PassengerDTO;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.PassengerUpdateDTO;
import com.modsen.cabaggregator.passengerservice.dto.PassengerViewingDTO;
import com.modsen.cabaggregator.passengerservice.exception.EmailIsAlreadyExistsException;
import com.modsen.cabaggregator.passengerservice.exception.PassengerNotFoundException;
import com.modsen.cabaggregator.passengerservice.exception.PhoneIsAlreadyExistsException;
import com.modsen.cabaggregator.passengerservice.mapper.PassengerMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.repository.PassengerRepository;
import com.modsen.cabaggregator.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultPassengerService implements PassengerService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPassengerService.class);
    public static final String PASSENGER_WAS_NOT_FOUND = "Passenger with %s was not found";

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public Page<PassengerViewingDTO> findAll(Integer page, Integer size, PassengerSortCriteria sort) {
        return passengerRepository.findAll(
                PageRequest.of(page, size, Sort.by(sort.getOrder(), sort.getField().getFiledName()))
        ).map(passengerMapper::toPassengerViewingDTO);
    }

    @Override
    public PassengerViewingDTO save(PassengerDTO passengerDTO) {
        validateUniqueData(passengerDTO);
        final UUID id = UUID.randomUUID();

        LOG.info("Save new passenger. ID: {}", id);
        return passengerMapper.toPassengerViewingDTO(
                passengerRepository.save(
                        Passenger.builder()
                                .id(id)
                                .name(passengerDTO.getName())
                                .surname(passengerDTO.getSurname())
                                .email(passengerDTO.getEmail().toLowerCase())
                                .phone(passengerDTO.getPhone())
                                .active(true)
                                .build()
                )
        );
    }

    @Override
    public void delete(UUID id) {
        passengerRepository.deleteById(id);
        LOG.info("Delete passenger. ID: {}", id);
    }

    @Override
    public PassengerViewingDTO findById(UUID id) {
        return passengerMapper.toPassengerViewingDTO(
                findEntityById(id)
        );
    }

    @Override
    public PassengerViewingDTO update(UUID id, PassengerUpdateDTO passengerDTO) {
        Passenger passenger = findEntityById(id);

        passenger.setName(passengerDTO.getName());
        passenger.setSurname(passengerDTO.getSurname());
        updatePhone(passengerDTO.getPhone(), passenger);
        updateEmail(passengerDTO.getEmail(), passenger);

        return passengerMapper.toPassengerViewingDTO(
                passengerRepository.save(passenger)
        );
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

    private void validateUniqueData(PassengerDTO passengerDTO) {
        throwExceptionIfEmailExists(passengerDTO.getEmail());
        throwExceptionIfPhoneExists(passengerDTO.getPhone());
    }

    Passenger findEntityById(UUID id) throws PassengerNotFoundException {
        return passengerRepository.findById(id)
                .orElseThrow(() ->
                        new PassengerNotFoundException(String.format(PASSENGER_WAS_NOT_FOUND, id))
                );
    }

    void throwExceptionIfPassengerDoesNotExist(UUID passengerId) {
        if (!passengerRepository.existsById(passengerId)) {
            throw new PassengerNotFoundException(String.format(PASSENGER_WAS_NOT_FOUND, passengerId));
        }
    }

}
