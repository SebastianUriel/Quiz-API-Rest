package com.test.main.testrest.config.exception;

public class GenericException extends RuntimeException {
    public GenericException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
