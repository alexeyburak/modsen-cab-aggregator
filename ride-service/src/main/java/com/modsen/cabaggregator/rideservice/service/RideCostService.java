package com.modsen.cabaggregator.rideservice.service;

import java.math.BigDecimal;

public interface RideCostService {
    BigDecimal getInitialRideCost();

    BigDecimal getFinalRideCostWithPromo(BigDecimal initialCost, String promo);
}
