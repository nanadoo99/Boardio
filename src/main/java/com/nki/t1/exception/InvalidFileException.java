package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;

public class InvalidFileException extends BadRequestException {

    public InvalidFileException(ErrorType errorType) {
        super(errorType);
    }

    public InvalidFileException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public InvalidFileException(ErrorType errorType, ObjDto... objects) {
        super(errorType, objects);
    }
}
