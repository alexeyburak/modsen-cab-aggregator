package com.modsen.cabaggregator.passengerservice.mapper;

import com.modsen.cabaggregator.common.config.MapperConfig;
import com.modsen.cabaggregator.passengerservice.dto.PassengerResponse;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PassengerMapper {
    PassengerResponse toPassengerResponse(Passenger passenger);
}
