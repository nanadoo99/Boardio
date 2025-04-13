package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;

public class InvalidBlockException extends BadRequestException {
    public InvalidBlockException(ErrorType errorType) {
        super(errorType);
    }
    public InvalidBlockException(ErrorType errorType, ObjDto... objects) {
        super(errorType, objects);
    }
}
