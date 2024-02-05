package com.modsen.cabaggregator.rideservice.mapper;

import com.modsen.cabaggregator.common.config.MapperConfig;
import com.modsen.cabaggregator.rideservice.dto.RideResponse;
import com.modsen.cabaggregator.rideservice.model.Ride;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RideMapper {
    RideResponse toRideResponse(Ride ride);
}
