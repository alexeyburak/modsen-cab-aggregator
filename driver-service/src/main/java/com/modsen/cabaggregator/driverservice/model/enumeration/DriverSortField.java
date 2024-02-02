package com.modsen.cabaggregator.driverservice.model.enumeration;

import lombok.Getter;

@Getter
public enum DriverSortField {
    NAME("name"),
    SURNAME("surname");

    private final String fieldName;

    DriverSortField(String fieldName) {
        this.fieldName = fieldName;
    }

}
