package com.modsen.cabaggregator.common.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class GlobalConstants {
    public final String DEFAULT_PAGE = "0";
    public final String DEFAULT_SIZE = "10";
    public final String PHONE_REGEXP = "^(80(29|44|33|25)\\d{7})$";
    public final String DECIMAL_FORMAT_PATTERN = "#.#";
    public final int DEFAULT_SCORE = 5;
    public final String BYN = "byn";
    public final BigDecimal DEFAULT_CUSTOMER_BALANCE = BigDecimal.valueOf(1000L);
}
