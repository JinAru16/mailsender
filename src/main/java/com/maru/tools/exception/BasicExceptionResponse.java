package com.maru.tools.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasicExceptionResponse {

    private Integer errorCode;
    private String errorMessage;

}
