package com.solutionchallenge.factchecker.learn.dto.response;

import com.solutionchallenge.factchecker.learn.domain.QuizWord;


import java.sql.Timestamp;

public class QuizWordResponseDto {
    private Long id;
    private String quiz_word;
    private String mean;
    private Timestamp createdDate;

    public QuizWordResponseDto(QuizWord quizWord) {

    }
}
