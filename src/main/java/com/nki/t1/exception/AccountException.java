package com.nki.t1.exception;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AccountException extends AuthenticationException {
    private final int code;
    private final ObjDto[] objects;

    // ObjDto > redirectAttribute로 뷰에서 ${xxxDto} 사용됨.
    // AttributeNameUtils.getAttributeName() 참고.
    public AccountException(ErrorType errorType, String addedMessage, ObjDto... objects) {
        super(errorType.getMessage() + (addedMessage != null ? " " + addedMessage : ""));
        this.code = errorType.getCode();
        this.objects = objects != null ? objects : new ObjDto[0];  // Null-safe 처리
    }
}
