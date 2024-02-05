package com.modsen.cabaggregator.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final String DRIVERS_ENDPOINT = "/api/v1/drivers";
    public final String RATINGS_ENDPOINT = "/api/v1/drivers/{id}/rating";
    public final String DRIVERS = "Drivers";
    public final String RATINGS = "Ratings";
}
