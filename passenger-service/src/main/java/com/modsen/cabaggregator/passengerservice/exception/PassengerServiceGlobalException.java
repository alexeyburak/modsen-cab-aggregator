package com.modsen.cabaggregator.passengerservice.exception;

import com.modsen.cabaggregator.common.exception.CabAggregatorGlobalException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerServiceGlobalException extends CabAggregatorGlobalException {

    private static final String GLOBAL_CODE = "Something went wrong in passenger service";

    PassengerServiceGlobalException(String message) {
        super(message, GLOBAL_CODE);
    }

}
