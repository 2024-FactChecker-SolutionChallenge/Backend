package com.solutionchallenge.factchecker.learn.service;

import com.solutionchallenge.factchecker.learn.domain.*;
import com.solutionchallenge.factchecker.learn.dto.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//Module Service
@Service
public class QuizWordService {
    private final QuizWordRepository quizWordRepository;

    @Autowired
    public QuizWordService(QuizWordRepository quizWordRepository) {
        this.quizWordRepository = quizWordRepository;
    }

    public List<QuizWordResponseDto> getRandomQuiz() {
        // QuizWordRepository를 통해 랜덤으로 4개의 단어를 가져오기
        List<QuizWord> quizWords = quizWordRepository.findRandomFourWords();

        return quizWords.stream()
            .map(QuizWordResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public DailyQuizResponseDto updateScore(Long userId, String day, int dailyScore) {
        Optional<User> optionalUser = userRepoisitory.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // TODO : user엔티티 클래스에 updateScore메서드 밑 getDailyScore 메서드 작성할 것
            /*
            public void updateScore(String day, int dailyScore) {
                // 날짜에 대한 일일 점수 업데이트 또는 추가
                this.dailyScore.put(day, dailyScore);
            }
             */
            user.updateScore(day, dailyScore);
            User updatedUser = userRepository.save(user);
            return new DailyQuizResponseDto(updatedUser);
        } else {
            // 사용자를 찾을 수 없는 경우에 대한 예외 처리
            throw ResponseEntity.badRequest().body("User not found");
        }
    }
}
