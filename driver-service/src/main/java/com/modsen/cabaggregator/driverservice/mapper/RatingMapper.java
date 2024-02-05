package com.modsen.cabaggregator.driverservice.mapper;

import com.modsen.cabaggregator.common.config.MapperConfig;
import com.modsen.cabaggregator.driverservice.dto.RatingResponse;
import com.modsen.cabaggregator.driverservice.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = DriverMapper.class)
public interface RatingMapper {
    @Mapping(source = "driver", target = "driver")
    RatingResponse toRatingResponse(Rating rating);
}
