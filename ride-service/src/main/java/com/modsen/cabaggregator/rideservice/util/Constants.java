package com.modsen.cabaggregator.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final String PROMO_CODES_ENDPOINT = "/api/v1/promos";
    public final String RIDES_ENDPOINT = "/api/v1/rides";
    public final String NAME_MAPPING = "/{name}";
    public final String HISTORY_MAPPING = "/history";
    public final String ID_MAPPING = "/{id}";
    public final String ID_STATUS_MAPPING = "/{id}/pay";
    public final String ID_REJECT_MAPPING = "/{id}/reject";
    public final String ID_START_MAPPING = "/{id}/start";
    public final String ID_FINISH_MAPPING = "/{id}/finish";
    public final String PROMO_CODES = "Promo codes";
    public final String RIDES = "Rides";
}