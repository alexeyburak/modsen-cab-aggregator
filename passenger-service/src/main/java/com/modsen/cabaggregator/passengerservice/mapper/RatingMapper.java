package com.modsen.cabaggregator.passengerservice.mapper;

import com.modsen.cabaggregator.common.config.MapperConfig;
import com.modsen.cabaggregator.passengerservice.dto.RatingResponse;
import com.modsen.cabaggregator.passengerservice.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = PassengerMapper.class)
public interface RatingMapper {

    @Mapping(source = "passenger", target = "passenger")
    RatingResponse toRatingResponse(Rating rating);

}
