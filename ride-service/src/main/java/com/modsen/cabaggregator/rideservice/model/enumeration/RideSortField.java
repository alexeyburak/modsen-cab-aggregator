package com.modsen.cabaggregator.rideservice.model.enumeration;

import lombok.Getter;

@Getter
public enum RideSortField {
    COST("finalCost"),
    DATE("dateAt");

    private final String fieldName;

    RideSortField(String fieldName) {
        this.fieldName = fieldName;
    }

}
