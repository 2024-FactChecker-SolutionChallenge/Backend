package com.solutionchallenge.factchecker.api.Learn.exception;

import com.solutionchallenge.factchecker.global.dto.response.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LearnExceptionHandler  extends RuntimeException {
    @Builder
    public LearnExceptionHandler(String message) { super(message); }


}
