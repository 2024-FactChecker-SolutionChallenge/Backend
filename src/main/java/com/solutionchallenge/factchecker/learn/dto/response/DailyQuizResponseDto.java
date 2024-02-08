package com.solutionchallenge.factchecker.learn.dto.response;

import com.solutionchallenge.factchecker.User.domain.DailyScore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyQuizResponseDto {
    private Long userId;
    private int dailyScore;
    private DailyScore dailyScoreObject; // 데일리 스코어 정보

    // 생성자, getter, setter 등 필요한 메서드들 추가

    public DailyQuizResponseDto(User user) {
        this.userId = user.getUserId();
        this.dailyScore = user.getDailyScore();
        this.dailyScoreObject = user.getDailyScoreObject();
    }
}
