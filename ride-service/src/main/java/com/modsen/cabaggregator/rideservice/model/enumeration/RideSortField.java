package com.modsen.cabaggregator.rideservice.model.enumeration;

import lombok.Getter;

@Getter
public enum RideSortField {
    COST("final_cost"),
    DATE("date_at");

    private final String fieldName;

    RideSortField(String fieldName) {
        this.fieldName = fieldName;
    }

}
