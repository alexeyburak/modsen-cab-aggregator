package com.modsen.cabaggregator.rideservice.mapper;

import com.modsen.cabaggregator.rideservice.config.MapperConfig;
import com.modsen.cabaggregator.rideservice.dto.PromoCodeResponse;
import com.modsen.cabaggregator.rideservice.model.PromoCode;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PromoCodeMapper {
    PromoCodeResponse toPromoCodeResponse(PromoCode promoCode);
}
