package com.nki.t1.domain;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import java.util.Locale;

@Getter
public class ErrorDetail {
    private String objectName;
    private String field;
    private String code;
    private String message;

    public ErrorDetail(builder builder) {
        this.objectName = builder.objectName;
        this.field = builder.field;
        this.code = builder.code;
        this.message = builder.message;
    }

    public static class builder {
        private String objectName;
        private String field;
        private String code;
        private String message;

        public builder(FieldError fieldError, MessageSource messageSource, Locale locale) {
            this.objectName = fieldError.getObjectName();
            this.field = fieldError.getField();
            this.code = fieldError.getCode();
            this.message = messageSource.getMessage(fieldError, locale);

            // {0}을 max 값으로 치환하기 위해 arguments 추가
            Object[] arguments = fieldError.getArguments();
            if (arguments != null && arguments.length > 1) {
                this.message = messageSource.getMessage(fieldError.getDefaultMessage(), new Object[]{arguments[1]}, locale);
            } else {
                this.message = messageSource.getMessage(fieldError, locale);
            }
        }

        public ErrorDetail build() {
            return new ErrorDetail(this);
        }
    }

}
