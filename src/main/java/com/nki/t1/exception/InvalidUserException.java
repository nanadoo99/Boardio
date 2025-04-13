package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;

public class InvalidUserException extends AccountException {
    public InvalidUserException(ErrorType errorType) {
        this(errorType, new ObjDto[0]);
    }

    public InvalidUserException(ErrorType errorType, ObjDto... objects) {
        this(errorType, "", objects);
    }

    public InvalidUserException(ErrorType errorType, String addedMessage, ObjDto... objects) {
        super(errorType, (addedMessage != null ? " " + addedMessage : ""), objects);
    }
}
