package com.solutionchallenge.factchecker.api.Learn.dto.response;

import com.solutionchallenge.factchecker.api.Learn.entity.QuizWord;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizWordResponseDto {
    private Long id;
    private String quiz_word;
    private String mean;
    private Timestamp createdDate;

    public QuizWordResponseDto(QuizWord quizWord) {
        this.id = quizWord.getQuiz_wordId();
        this.quiz_word = quizWord.getQuiz_word();
        this.mean = quizWord.getMean();
        this.createdDate = quizWord.getCreatedDate();
    }
}
