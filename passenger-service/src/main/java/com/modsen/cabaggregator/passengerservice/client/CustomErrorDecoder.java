package com.modsen.cabaggregator.passengerservice.client;

import com.modsen.cabaggregator.passengerservice.exception.ThirdPartyApiException;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String key, Response response) {
        FeignException exception = FeignException.errorStatus(key, response);
        int status = response.status();
        if (status >= 500) {
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    null,
                    response.request());
        }
        if (status == 400) {
            throw new ThirdPartyApiException();
        }
        return exception;
    }

}
