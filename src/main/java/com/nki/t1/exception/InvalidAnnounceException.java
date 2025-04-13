package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;

public class InvalidAnnounceException extends BadRequestException {
    public InvalidAnnounceException(ErrorType errorType) {
        super(errorType);
    }
    public InvalidAnnounceException(ErrorType errorType, ObjDto... objects) {
        super(errorType, objects);
    }
}
