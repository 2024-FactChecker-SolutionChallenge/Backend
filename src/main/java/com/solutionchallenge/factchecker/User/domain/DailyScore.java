package com.solutionchallenge.factchecker.User.domain;
import java.util.Map;

public class DailyScore {
    private Map<String, Integer> scores; // "월", "화", ..., "일"에 대한 점수 정보

    // 생성자, getter, setter 등 필요한 메서드들 추가

    // 예시 데이터를 이용한 생성자
    public DailyScore() {
        this.scores = Map.of(
                "월", 0,
                "화", 0,
                "수", 0,
                "목", 0,
                "금", 0,
                "토", 0,
                "일", 0
        );
    }
}