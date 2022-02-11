package com.test.main.testrest.config.exception;

public enum ErrorCode {

    INTERNAL_ERROR(100, "Internal Error - Issue not mapped", "INTERNAL ERROR"),
    BAD_REQUEST(101, "Bad request - Request obtained is wrong", "BAD_REQUEST"),
    CONFLICT(102, "Conflict - The logic in the request was failed", "CONFLICT"),
    DATABASE_ERROR(103, "Database error - Check the connectivity of the database", "DATABASE_ERROR");

    private final int code;
    private final String message;
    private final String type;

    ErrorCode(int code, String message, String type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

}
