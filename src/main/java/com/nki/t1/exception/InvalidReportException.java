package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;

public class InvalidReportException extends BadRequestException {
    public InvalidReportException(ErrorType errorType) {
        super(errorType);
    }
    public InvalidReportException(ErrorType errorType, ObjDto... objects) {
        super(errorType, objects);
    }
}
