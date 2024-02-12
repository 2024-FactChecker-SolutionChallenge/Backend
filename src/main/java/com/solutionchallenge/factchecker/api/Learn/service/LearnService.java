package com.solutionchallenge.factchecker.api.Learn.service;

import com.solutionchallenge.factchecker.api.Member.entity.UserDetailsImpl;
import com.solutionchallenge.factchecker.api.Learn.dto.response.DailyQuizResponseDto;
import com.solutionchallenge.factchecker.api.Learn.dto.response.QuizWordResponseDto;
import com.solutionchallenge.factchecker.api.Learn.dto.response.WordResponseDto;
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
    public List<WordResponseDto> getWordList(String member_id) {
        return wordService.getWordList(member_id);
    }

    //wordservice 사용 - 단어 앎 상태 변경
    public WordResponseDto updateWord( Long wordId, String member_id) {
        return wordService.updateWordStatus(wordId, member_id);

    }

    //wordservice 사용 - 모르는 단어 플립카드로 제공
    public List<WordResponseDto> getUnknownWordList(String member_id) {
        return wordService.getUnknownWordList(member_id);
    }


    //quizWordService 사용 - 랜덤 퀴즈단어 제공
    public List<QuizWordResponseDto> getQuiz() {
        return quizWordService.getRandomQuiz();
    }

    //quizWordService 사용 - 퀴즈 점수 저장
    public DailyQuizResponseDto updateScore(String member_id, String day, int Score) {
        return quizWordService.updateScore(member_id, day, Score);
    }

}
