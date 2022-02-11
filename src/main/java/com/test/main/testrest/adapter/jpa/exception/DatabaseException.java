package com.test.main.testrest.adapter.jpa.exception;

import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;

public class DatabaseException extends GenericException {

    public DatabaseException(ErrorCode errorCode) {
        super(errorCode);
    }

}
