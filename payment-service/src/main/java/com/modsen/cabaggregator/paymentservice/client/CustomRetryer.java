package com.modsen.cabaggregator.paymentservice.client;

import com.modsen.cabaggregator.paymentservice.exception.FeignClientRetryableException;
import feign.RetryableException;
import feign.Retryer;

public class CustomRetryer implements Retryer {

    @Override
    public void continueOrPropagate(RetryableException e) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new FeignClientRetryableException();
        }
    }

    @Override
    public Retryer clone() {
        return new CustomRetryer();
    }

}
