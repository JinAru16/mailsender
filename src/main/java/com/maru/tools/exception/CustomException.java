package com.maru.tools.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{

    private final HttpStatus status;
    private final Integer errorCode;
    private final String errorMessage;

    public CustomException(BasicExceptionEnum basicExceptionEnum) {
        this.status = basicExceptionEnum.getStatus();
        this.errorCode = basicExceptionEnum.getErrorCode();
        this.errorMessage = basicExceptionEnum.getErrorMessage();
    }

    public CustomException(BasicExceptionEnum basicExceptionEnum, String errorMessage) {
        this.status = basicExceptionEnum.getStatus();
        this.errorCode = basicExceptionEnum.getErrorCode();
        if(StringUtils.hasText(errorMessage)) {
            this.errorMessage = errorMessage;
        }else {
            this.errorMessage = basicExceptionEnum.getErrorMessage();
        }
    }
}
