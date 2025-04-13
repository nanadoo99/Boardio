package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;

public class InvalidCommentException extends BadRequestException {
    public InvalidCommentException(ErrorType errorType) {
        super(errorType);
    }
    public InvalidCommentException(ErrorType errorType, ObjDto... objects) {
        super(errorType, objects);
    }
}
