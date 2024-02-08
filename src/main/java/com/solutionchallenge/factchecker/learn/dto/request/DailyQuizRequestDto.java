package com.solutionchallenge.factchecker.learn.dto.request;

import lombok.Getter;

@Getter
public class DailyQuizRequestDto {
    private String day; //예시: "월", "화" , "수"
    private int score;

    public DailyQuizRequestDto( String day, int score) {
        this.day = day;
        this.score = score;
    }
}
