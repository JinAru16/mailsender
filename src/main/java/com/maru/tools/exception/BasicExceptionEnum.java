package com.maru.tools.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BasicExceptionEnum {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "인증되지 않은 사용자입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다"),
    HANDLER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "요청 페이지를 찾을 수 없습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "요청 데이터를 찾을 수 없습니다"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "허용되지 않은 요청입니다"),
    CONFLICT(HttpStatus.CONFLICT, 409, "중복된 자원입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버가 원활하지 않습니다 \n 문제가 지속되는 경우 관리자에게 문의해주세요")
    ;

    private final HttpStatus status;
    private final Integer errorCode;
    private final String errorMessage;

}
