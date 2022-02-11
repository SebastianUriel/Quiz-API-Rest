package com.test.main.testrest.config.exception;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Error> handleException(Throwable ex) {
        log.error("There is an exception not mapped in the code, ERROR={}", ex.getMessage());
        return buildError(ErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Error> buildError(ErrorCode errorCode, HttpStatus httpStatus) {
        return new ResponseEntity<Error>(Error.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .type(errorCode.getType())
                .build(),
                httpStatus
        );
    }

    @Data
    @Builder
    static class Error {
        private int code;
        private String message;
        private String type;
    }

}
