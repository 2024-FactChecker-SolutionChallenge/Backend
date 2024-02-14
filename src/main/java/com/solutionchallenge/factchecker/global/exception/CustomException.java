package com.solutionchallenge.factchecker.global.exception;

import com.solutionchallenge.factchecker.global.dto.response.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomException  extends RuntimeException {
    @Builder
    public CustomException(String message) { super(message); }
}
