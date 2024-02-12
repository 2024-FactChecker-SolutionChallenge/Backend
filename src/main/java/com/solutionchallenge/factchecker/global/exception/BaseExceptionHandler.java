package com.solutionchallenge.factchecker.global.exception;

import com.solutionchallenge.factchecker.api.Learn.exception.LearnExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    /**
     * BusinessException에서 발생한 에러
     *
     * @param ex LearnException
     * @return ResponseEntity
     */
    @ExceptionHandler(LearnExceptionHandler.class)
    public ResponseEntity<?> handleLearnException(LearnExceptionHandler ex) {
        log.debug("===========================================================");
        log.debug("여기로 오는가?!");
        log.debug("===========================================================");

        final GlobalResponse<Object> response = GlobalResponse.fail();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
