package com.nki.t1.advice;

import com.nki.t1.domain.ErrorResponse;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.ObjDto;
import com.nki.t1.exception.AccountException;
import com.nki.t1.exception.BadRequestException;
import com.nki.t1.exception.UnauthorizedException;
import com.nki.t1.utils.AttributeNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final AttributeNameUtils attributeNameUtils;

    public GlobalExceptionHandler(AttributeNameUtils attributeNameUtils) {
        this.attributeNameUtils = attributeNameUtils;
    }

    @ExceptionHandler(BadRequestException.class)
    public Object badRequestExceptionHandler(BadRequestException e, HttpServletRequest request, RedirectAttributes reAttr) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        if (e.getObjects() != null) {
            for (ObjDto object : e.getObjects()) {
                reAttr.addFlashAttribute(attributeNameUtils.getAttributeName(object), object);
            }
        }

        return getResponse(request, reAttr, errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Object unauthorizedExceptionHandler(UnauthorizedException e, HttpServletRequest request, RedirectAttributes reAttr) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());

        return getResponse(request, reAttr, errorResponse);
    }

    @ExceptionHandler(AccountException.class)
    public Object accountExceptionHandler(AccountException e, HttpServletRequest request, RedirectAttributes reAttr) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        if (e.getObjects() != null) {
            for (ObjDto object : e.getObjects()) {
                reAttr.addFlashAttribute(attributeNameUtils.getAttributeName(object), object);
            }
        }

        return getResponse(request, reAttr, errorResponse);
    }

    // 검증 경과에 대해여 ResponseEntity로 응답하는 경우.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);

        log.info("GlobalExceptionHandler.handleValidationExceptions");
        Map<String, String> errors = new HashMap<>();
        final String[] errorMessage = {"입력값이 올바르지 않습니다."};

        e.getBindingResult().getFieldErrors().forEach((FieldError error) -> {
            String fieldName = error.getField();
            errorMessage[0] = error.getDefaultMessage();
            errors.put(fieldName, errorMessage[0]);
        });

        ErrorResponse errorResponse = new ErrorResponse(999, errorMessage[0]);
        return getErrorResponseEntity(errorResponse);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Map<String, Object>> handleMultipartException(MultipartException e) {
        log.error(e.getMessage(), e);
        log.info("AdminAnnounceController.handleMultipartException");
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("uploaded", 0);
        errorResponse.put("error", Map.of("message", ErrorType.FILE_SIZE_EXCEEDED.getMessage() + " 최대 1MB 가능")); // 사용자에게 표시될 메시지
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public Object accountExceptionHandler(RuntimeException e, HttpServletRequest request, RedirectAttributes reAttr) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorType.REQUEST_FAILED);
        log.error(e.getMessage(), e);

        return getResponse(request, reAttr, errorResponse);
    }

    private Object getResponse(HttpServletRequest request, RedirectAttributes reAttr, ErrorResponse errorResponse) {
        if (isAjax(request)) {
            log.info("----- AJAX");
            return getErrorResponseEntity(errorResponse);
        } else {
            log.info("----- General");
            return getErrorString(request, reAttr, errorResponse);
        }
    }

    private static String getErrorString(HttpServletRequest request, RedirectAttributes reAttr, ErrorResponse errorResponse) {
        HttpSession session = request.getSession();
        session.setAttribute("uploadError", errorResponse);
        reAttr.addFlashAttribute(errorResponse);
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/";
        }
        log.debug("----- Referer: {}", referer);
        return "redirect:" + referer;
    }

    private static ResponseEntity<Map<String, Object>> getErrorResponseEntity(ErrorResponse errorResponse) {
        Map<String, Object> map = new HashMap<>();
        map.put("errorResponse", errorResponse);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    private boolean isAjax(HttpServletRequest request) {
        String ajaxHeader = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(ajaxHeader);
    }


}