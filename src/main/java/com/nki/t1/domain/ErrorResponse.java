package com.nki.t1.domain;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private int code;
    private String message;

    public ErrorResponse(int errorCode, String message) {
        this.code = errorCode;
        this.message = message;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(ErrorType errorType) {
        this(errorType.getCode(), errorType.getMessage());
    }
}
