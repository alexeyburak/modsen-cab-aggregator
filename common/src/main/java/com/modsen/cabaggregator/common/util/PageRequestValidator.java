package com.modsen.cabaggregator.common.util;

import com.modsen.cabaggregator.common.exception.InvalidPageRequestException;

public final class PageRequestValidator {

    private PageRequestValidator() {
    }

    public static void validatePageRequestParameters(Integer page, Integer size) throws InvalidPageRequestException {
        if (page < 0 || size < 0) {
            throw new InvalidPageRequestException();
        }
    }

}
