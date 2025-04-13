package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;

public class InvalidPostException extends BadRequestException {
    public InvalidPostException(ErrorType errorType) {
        super(errorType);
    }

    public InvalidPostException(ErrorType errorType, ObjDto... objects) {
        super(errorType, objects);
    }
}
