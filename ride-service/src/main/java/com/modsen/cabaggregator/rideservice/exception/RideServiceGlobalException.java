package com.modsen.cabaggregator.rideservice.exception;

import com.modsen.cabaggregator.common.exception.CabAggregatorGlobalException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RideServiceGlobalException extends CabAggregatorGlobalException {

    private static final String GLOBAL_CODE = "Something went wrong in ride service";

    RideServiceGlobalException(String message) {
        super(message, GLOBAL_CODE);
    }

}
