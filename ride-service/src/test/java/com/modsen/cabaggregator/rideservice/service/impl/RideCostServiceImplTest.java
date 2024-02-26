package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.rideservice.service.PromoCodeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class RideCostServiceImplTest {

    private static final BigDecimal MIN_RIDE_COST = BigDecimal.valueOf(1000L);
    private static final BigDecimal MAX_RIDE_COST = BigDecimal.valueOf(10000L);

    @Mock
    private PromoCodeService promoCodeService;

    @InjectMocks
    private RideCostServiceImpl rideCostService;

    @Test
    void testGetInitialRideCost() {
        final BigDecimal actual = rideCostService.getInitialRideCost();

        Assertions.assertThat(actual)
                .isGreaterThanOrEqualTo(MIN_RIDE_COST)
                .isLessThanOrEqualTo(MAX_RIDE_COST);
    }

    @Test
    void testGetFinalRideCostWithPromo_WithoutPromo() {
        final BigDecimal initialCost = BigDecimal.valueOf(2000L);
        final String promo = "";

        BigDecimal actual = rideCostService.getFinalRideCostWithPromo(initialCost, promo);

        Assertions.assertThat(actual).isEqualTo(initialCost);
    }

    @Test
    void testGetFinalRideCostWithPromo_WithPromo() {
        final String promo = "promo-1";
        final BigDecimal promoCodeValue = BigDecimal.valueOf(500L);
        final BigDecimal initial = BigDecimal.valueOf(2000L);
        final BigDecimal expected = initial.subtract(promoCodeValue);
        Mockito.when(promoCodeService.getPromoCodeValue(promo)).thenReturn(promoCodeValue);

        BigDecimal actual = rideCostService.getFinalRideCostWithPromo(initial, promo);

        Assertions.assertThat(expected).isGreaterThan(BigDecimal.ZERO);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}
