package com.gonaeun.springjwtproject.common.exception;

public class CustomException extends RuntimeException {
    private final ErrorCode code;
    public CustomException(ErrorCode code) {
        super(code.getDefaultMessage());
        this.code = code;
    }
    public CustomException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
    public ErrorCode getCode() { return code; }
}
