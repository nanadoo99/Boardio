package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class BadRequestException extends RuntimeException {
    private final int code;
    private Set<ObjDto> objects;
    private ErrorType errorType;

    // ObjDto > redirectAttribute로 뷰에서 ${xxxDto} 사용됨.
    // AttributeNameUtils.getAttributeName() 참고.
    public BadRequestException(ErrorType errorType, ObjDto... objects) {
        super(errorType.getMessage());
        this.code = errorType.getCode();
        this.objects = new HashSet<>();
        this.errorType = errorType;

        if (objects != null) {
            for (ObjDto objDto : objects) {
                this.objects.add(objDto);
            }
        }
    }

    public BadRequestException(ErrorType errorType) {
        this(errorType, new ObjDto[0]);
    }

    public BadRequestException(ErrorType errorType, String message) {
        super(errorType.getMessage() + message);
        this.code = errorType.getCode();
    }

    public void addObject(ObjDto obj) {
        if (obj != null) {
            this.objects.add(obj);
        }
    }
}
