package com.modsen.cabaggregator.rideservice.model.enumeration;

import lombok.Getter;

@Getter
public enum RideStatus {
    NOT_PAID,
    PAID,
    STARTED,
    FINISHED,
    REJECTED
}
