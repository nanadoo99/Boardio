package com.nki.t1.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class CustomMultipartResolver extends CommonsMultipartResolver {

    @Override
    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        try {
            return super.parseRequest(request);
        } catch (MaxUploadSizeExceededException e) {
            // request에 예외 정보를 기록해서 나중에 Controller 또는 Filter에서 확인 가능
            request.setAttribute("uploadSizeExceeded", true);
            log.debug("Max upload size exceeded");
            throw e;
        }
    }
}