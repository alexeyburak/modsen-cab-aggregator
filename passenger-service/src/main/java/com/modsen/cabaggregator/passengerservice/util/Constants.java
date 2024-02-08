package com.modsen.cabaggregator.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final String PASSENGERS_ENDPOINT = "/api/v1/passengers";
    public final String RATINGS_ENDPOINT = "/api/v1/passengers/{id}/rating";
    public final String ID_MAPPING = "/{id}";
    public final String AVERAGE_MAPPING = "/average";
    public final String PASSENGERS = "Passengers";
    public final String RATINGS = "Ratings";
}