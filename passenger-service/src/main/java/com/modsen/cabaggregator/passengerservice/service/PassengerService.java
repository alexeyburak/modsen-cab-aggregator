package com.modsen.cabaggregator.passengerservice.service;

import com.modsen.cabaggregator.passengerservice.dto.PassengerDTO;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.PassengerUpdateDTO;
import com.modsen.cabaggregator.passengerservice.dto.PassengerViewingDTO;
import com.modsen.cabaggregator.passengerservice.exception.PassengerNotFoundException;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PassengerService {
    Page<PassengerViewingDTO> findAll(Integer page, Integer size, PassengerSortCriteria sort);
    PassengerViewingDTO save(PassengerDTO passengerDTO);
    void delete(UUID id);
    PassengerViewingDTO findById(UUID id) throws PassengerNotFoundException;
    PassengerViewingDTO update(UUID id, PassengerUpdateDTO passengerUpdateDTO) throws PassengerNotFoundException;
}
