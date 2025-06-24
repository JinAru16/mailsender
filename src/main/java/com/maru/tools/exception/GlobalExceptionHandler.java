package com.maru.tools.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.security.InvalidParameterException;

import static com.maru.tools.exception.BasicExceptionEnum.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CustomException.class
    })
    public ResponseEntity<?> handleCustomException(CustomException ce, HttpServletRequest request) {

        log.error("Custom Exception for - {}", createRequestInfo(request));
        log.error("Error Message - {}", ce.getErrorMessage());

        return ResponseEntity
                .status(ce.getStatus())
                .body(BasicExceptionResponse.builder()
                        .errorCode(ce.getErrorCode())
                        .errorMessage(ce.getErrorMessage())
                        .build()
                );
    }


    @ExceptionHandler({
            MethodArgumentNotValidException.class, // @Valid, @Validated 유효성 검사 실패 시
            MethodArgumentTypeMismatchException.class, // @RequestParam, @PathVariable에 잘못된 값 전달 시
            MissingServletRequestParameterException.class, // @RequestParam 미전달 시
            MissingPathVariableException.class, // @PathVariable 미전달 시
            HttpMessageNotReadableException.class, // Request Body의 JSON 데이터를 파싱하지 못하는 경우
            ConstraintViolationException.class, // 엔티티 제약 조건 예외 발생 시
            InvalidParameterException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<?> handleInvalidParameter(Exception e, HttpServletRequest request) {

        log.error("Invalid Parameter for - {}", createRequestInfo(request));
        log.error("Error Message - {}", e.getMessage(), e);

        return ResponseEntity
                .status(INVALID_PARAMETER.getStatus())
                .body(BasicExceptionResponse.builder()
                        .errorCode(INVALID_PARAMETER.getErrorCode())
                        .errorMessage(INVALID_PARAMETER.getErrorMessage())
                        .build()
                );
    }

    @ExceptionHandler({
            NoHandlerFoundException.class
    })
    public ResponseEntity<?> handleNoHandlerFound(Exception e, HttpServletRequest request) {

        log.error("No Mapping for - {}", createRequestInfo(request));
        log.error("Error Message - {}", e.getMessage(), e);

        return ResponseEntity
                .status(HANDLER_NOT_FOUND.getStatus())
                .body(BasicExceptionResponse.builder()
                        .errorCode(HANDLER_NOT_FOUND.getErrorCode())
                        .errorMessage(HANDLER_NOT_FOUND.getErrorMessage())
                        .build()
                );
    }

    @ExceptionHandler({
            NoResourceFoundException.class
    })
    public ResponseEntity<?> handleNoResourceFound(Exception e, HttpServletRequest request) {

        log.error("No Resource for - {}", createRequestInfo(request));
        log.error("Error Message - {}", e.getMessage(), e);

        return ResponseEntity
                .status(RESOURCE_NOT_FOUND.getStatus())
                .body(BasicExceptionResponse.builder()
                        .errorCode(RESOURCE_NOT_FOUND.getErrorCode())
                        .errorMessage(RESOURCE_NOT_FOUND.getErrorMessage())
                        .build()
                );
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<?> handleMethodNotSupported(Exception e, HttpServletRequest request) {

        log.error("HTTP Method Not Supported for - {}", createRequestInfo(request));
        log.error("Error Message - {}", e.getMessage(), e);

        return ResponseEntity
                .status(METHOD_NOT_ALLOWED.getStatus())
                .body(BasicExceptionResponse.builder()
                        .errorCode(METHOD_NOT_ALLOWED.getErrorCode())
                        .errorMessage(METHOD_NOT_ALLOWED.getErrorMessage())
                        .build()
                );
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<?> handleInternalServerError(Exception e, HttpServletRequest request) {

        log.error("Server Error for - {}", createRequestInfo(request));
        log.error("Error Message - {}", e.getMessage(), e);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.getStatus())
                .body(BasicExceptionResponse.builder()
                        .errorCode(INTERNAL_SERVER_ERROR.getErrorCode())
                        .errorMessage(INTERNAL_SERVER_ERROR.getErrorMessage())
                        .build()
                );
    }

    // 로그용 요청 정보 생성
    private String createRequestInfo(HttpServletRequest request) {

        return request.getMethod() + ": " + request.getRequestURL();
    }

}
