package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;

public class InvalidEmailException extends BadRequestException {
    public InvalidEmailException(ErrorType errorType) {
        super(errorType);
    }
    public InvalidEmailException(ErrorType errorType, ObjDto... objects) {
        super(errorType, objects);
    }
}
