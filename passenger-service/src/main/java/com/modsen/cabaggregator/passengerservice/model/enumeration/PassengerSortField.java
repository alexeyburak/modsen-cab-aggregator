package com.modsen.cabaggregator.passengerservice.model.enumeration;

import lombok.Getter;

@Getter
public enum PassengerSortField {
    NAME("name"),
    SURNAME("surname"),
    EMAIL("email");

    private final String filedName;

    PassengerSortField(String filedName) {
        this.filedName = filedName;
    }

}
