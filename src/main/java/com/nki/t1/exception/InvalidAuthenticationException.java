package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;

public class InvalidAuthenticationException extends UnauthorizedException{
    public InvalidAuthenticationException(ErrorType errorType) {
        super(errorType);
    }
}
