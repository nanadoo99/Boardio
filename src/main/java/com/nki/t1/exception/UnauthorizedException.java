package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{
    private final int code;
    public UnauthorizedException(ErrorType errorType) {
        super(errorType.getMessage());
        this.code = errorType.getCode();
    }

}
