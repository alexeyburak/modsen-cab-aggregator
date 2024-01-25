package com.modsen.cabaggregator.passengerservice.mapper;

import com.modsen.cabaggregator.passengerservice.config.MapperConfig;
import com.modsen.cabaggregator.passengerservice.dto.PassengerViewingDTO;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PassengerMapper {
    PassengerViewingDTO toPassengerViewingDTO(Passenger passenger);
}
