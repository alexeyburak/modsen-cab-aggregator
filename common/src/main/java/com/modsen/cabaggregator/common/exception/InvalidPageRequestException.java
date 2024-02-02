package com.modsen.cabaggregator.common.exception;

public final class InvalidPageRequestException extends CabAggregatorGlobalException {
    public InvalidPageRequestException() {
        super("Invalid page request params");
    }
}
