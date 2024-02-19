package com.modsen.cabaggregator.rideservice.mapper;

import com.modsen.cabaggregator.common.config.MapperConfig;
import com.modsen.cabaggregator.rideservice.dto.DriverResponse;
import com.modsen.cabaggregator.rideservice.dto.PassengerResponse;
import com.modsen.cabaggregator.rideservice.dto.RideInfoResponse;
import com.modsen.cabaggregator.rideservice.dto.RideResponse;
import com.modsen.cabaggregator.rideservice.model.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RideMapper {
    RideResponse toRideResponse(Ride ride);

    @Mapping(source = "ride.id", target = "id")
    @Mapping(source = "ride.status", target = "status")
    RideInfoResponse toRideInfoResponse(Ride ride, DriverResponse driver, PassengerResponse passenger);
}
