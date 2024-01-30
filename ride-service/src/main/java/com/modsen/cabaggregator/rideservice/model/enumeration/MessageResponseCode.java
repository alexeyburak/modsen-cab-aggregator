package com.modsen.cabaggregator.rideservice.model.enumeration;

import lombok.Getter;

@Getter
public enum MessageResponseCode {
    REJECT_RIDE("r-1"),
    START_RIDE("r-2"),
    FINISH_RIDE("r-3");

    private final String globalCode;

    MessageResponseCode(String globalCode) {
        this.globalCode = globalCode;
    }

}
