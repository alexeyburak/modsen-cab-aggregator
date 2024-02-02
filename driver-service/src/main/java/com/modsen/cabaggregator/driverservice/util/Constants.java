package com.modsen.cabaggregator.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final String DRIVERS_ENDPOINT = "/api/v1/drivers";
    public final String RATINGS_ENDPOINT = "/api/v1/drivers/{id}/rating";
    public final String PHONE_REGEXP = "^(80(29|44|33|25)\\d{7})$";
    public final String DECIMAL_FORMAT_PATTERN = "#.#";
    public final int DEFAULT_SCORE = 5;
}
