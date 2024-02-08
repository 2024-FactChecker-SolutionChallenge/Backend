package com.solutionchallenge.factchecker.learn.service;

import com.solutionchallenge.factchecker.learn.dto.response.DailyQuizResponseDto;
import com.solutionchallenge.factchecker.learn.dto.response.QuizWordResponseDto;
import com.solutionchallenge.factchecker.learn.dto.response.WordResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//Component Service
@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class LearnService {

    private final WordService wordService;
    private final QuizWordService quizWordService;

    //wordservice 사용 - 저장된 단어들 조회
    public List<WordResponseDto> getWordList(Long userId) {
        return wordService.getWordList(userId);
    }

    //wordservice 사용 - 단어 앎 상태 변경
    public WordResponseDto updateWord( Long wordId, Long userId) {
        return wordService.updateWordStatus(wordId, userId);

    }

    //wordservice 사용 - 모르는 단어 플립카드로 제공
    public List<WordResponseDto> getUnknownWordList(Long userId) {
        return wordService.getUnknownWordList(userId);
    }


    //quizWordService 사용 - 랜덤 퀴즈단어 제공
    public List<QuizWordResponseDto> getQuiz() {
        return quizWordService.getRandomQuiz();
    }

    //userService 사용 - 퀴즈 점수 저장
    public DailyQuizResponseDto updateScore(Long userId, String day, int Score) {
        return quizWordService.updateScore(userId, day, Score);
    }

}
