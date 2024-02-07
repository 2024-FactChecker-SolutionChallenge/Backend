package com.solutionchallenge.factchecker.global.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private Boolean isSuccess;
    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
    private ErrorCode errorCode;

    //성공
    public BaseResponse(T result) {
        this.isSuccess = ErrorCode.SUCCESS.getIsSuccess();
        this.code = ErrorCode.SUCCESS.getCode();
        this.message = ErrorCode.SUCCESS.getMessage();
        this.result = result;
        this.errorCode = ErrorCode.SUCCESS;
    }

    // 오류 발생
    public BaseResponse(ErrorCode errorCode) {
        this.isSuccess = errorCode.getIsSuccess();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }

    public BaseResponse(ErrorCode errorCode, T result) {
        this.isSuccess = errorCode.getIsSuccess();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.result = result;
        this.errorCode = errorCode;
    }

    public BaseResponse(String successMessage) {
        this.isSuccess = true;
        this.code = ErrorCode.SUCCESS.getCode();
        this.message = successMessage;
        this.errorCode = ErrorCode.SUCCESS;
    }

}
