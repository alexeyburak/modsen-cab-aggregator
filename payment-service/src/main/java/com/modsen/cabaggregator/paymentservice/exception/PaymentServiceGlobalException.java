package com.modsen.cabaggregator.paymentservice.exception;

import com.modsen.cabaggregator.common.exception.CabAggregatorGlobalException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentServiceGlobalException extends CabAggregatorGlobalException {

    private static final String GLOBAL_CODE = "Something went wrong in payment service";

    PaymentServiceGlobalException(String message) {
        super(message, GLOBAL_CODE);
    }

}
