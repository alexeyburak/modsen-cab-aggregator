package com.modsen.cabaggregator.passengerservice.mapper;

import com.modsen.cabaggregator.passengerservice.config.MapperConfig;
import com.modsen.cabaggregator.passengerservice.dto.RatingViewingDTO;
import com.modsen.cabaggregator.passengerservice.model.Rating;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RatingMapper {

    RatingViewingDTO toRatingViewingDTO(Rating rating);

}
