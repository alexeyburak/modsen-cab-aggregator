package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.rideservice.service.PromoCodeService;
import com.modsen.cabaggregator.rideservice.service.RideCostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideCostServiceImpl implements RideCostService {

    private static final BigDecimal MIN_RIDE_COST = BigDecimal.valueOf(1000L);
    private static final BigDecimal MAX_RIDE_COST = BigDecimal.valueOf(10000L);

    private final PromoCodeService promoCodeService;

    @Override
    public BigDecimal getInitialRideCost() {
        log.debug("Generate initial ride cost");
        return generateRandomBigDecimal();
    }

    @Override
    public BigDecimal getFinalRideCostWithPromo(BigDecimal initialCost, String promo) {
        if (promo.isBlank()) {
            return initialCost;
        }
        BigDecimal costWithPromo = initialCost.subtract(promoCodeService.getPromoCodeValue(promo));
        log.debug("Generate ride cost with promo");
        return costWithPromo.signum() <= 0 ? BigDecimal.ZERO : costWithPromo;
    }

    private BigDecimal generateRandomBigDecimal() {
        return MIN_RIDE_COST.add(BigDecimal.valueOf(Math.random())
                        .multiply(MAX_RIDE_COST.subtract(MIN_RIDE_COST)))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
