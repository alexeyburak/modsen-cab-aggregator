package com.modsen.cabaggregator.paymentservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final String PAYMENTS_ENDPOINT = "/api/v1/payments";
    public final String PHONE_PATTERN_REGEXP = "^(80(29|44|33|25)\\d{7})$";
}
