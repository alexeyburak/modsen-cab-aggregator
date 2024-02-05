package com.modsen.cabaggregator.driverservice.exception;

import com.modsen.cabaggregator.common.exception.CabAggregatorGlobalException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverServiceGlobalException extends CabAggregatorGlobalException {

    private static final String GLOBAL_CODE = "Something went wrong in driver service";

    DriverServiceGlobalException(String message) {
        super(message, GLOBAL_CODE);
    }

}
