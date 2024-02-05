package com.modsen.cabaggregator.rideservice.service;

import com.modsen.cabaggregator.rideservice.dto.AllPromoCodesResponse;
import com.modsen.cabaggregator.rideservice.dto.CreatePromoCodeRequest;
import com.modsen.cabaggregator.rideservice.dto.UpdatePromoCodeRequest;
import com.modsen.cabaggregator.rideservice.dto.PromoCodeResponse;

import java.math.BigDecimal;

public interface PromoCodeService {
    AllPromoCodesResponse findAll(Integer page, Integer size);

    PromoCodeResponse create(CreatePromoCodeRequest createPromoCodeRequest);

    PromoCodeResponse update(String name, UpdatePromoCodeRequest promoCodeDTO);

    void delete(String name);

    BigDecimal getPromoCodeValue(String name);
}
