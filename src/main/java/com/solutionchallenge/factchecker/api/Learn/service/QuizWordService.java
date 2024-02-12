package com.solutionchallenge.factchecker.api.Learn.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.solutionchallenge.factchecker.api.Learn.entity.QuizWord;
import com.solutionchallenge.factchecker.api.Learn.exception.LearnExceptionHandler;
import com.solutionchallenge.factchecker.api.Learn.repository.QuizWordRepository;
import com.solutionchallenge.factchecker.api.Learn.dto.response.DailyQuizResponseDto;
import com.solutionchallenge.factchecker.api.Learn.dto.response.QuizWordResponseDto;
import com.solutionchallenge.factchecker.api.Member.entity.Member;
import com.solutionchallenge.factchecker.api.Member.repository.MemberRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

//Module Service
@Service
@Slf4j
public class QuizWordService {
    private final QuizWordRepository quizWordRepository;
    private final MemberRepository memberRepository;
    @PersistenceContext
    private final EntityManager entityManager; // EntityManager 추가

    @Autowired
    public QuizWordService(QuizWordRepository quizWordRepository, MemberRepository memberRepository, EntityManager entityManager) {
        this.quizWordRepository = quizWordRepository;
        this.memberRepository = memberRepository;
        this.entityManager = entityManager;
    }

    public List<QuizWordResponseDto> getRandomQuiz() {
        // QuizWordRepository를 통해 랜덤으로 4개의 단어를 가져오기
        List<QuizWord> quizWords = quizWordRepository.findRandomFourWords();

        return quizWords.stream()
            .map(QuizWordResponseDto::new)
                .collect(Collectors.toList());
    }

    public DailyQuizResponseDto updateScore(String member_id, String day, int score) {
        List<String> validDays = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
        if (!validDays.contains(day)) {
            throw new LearnExceptionHandler("요일로 잘못된 값이 들어왔습니다 : " + day);
        }
        try {
            Member member = memberRepository.findMemberById(member_id);
            Map<String, Integer> newmap = member.getDailyScore();
            log.info("초기 값: " + member);
            int existScore = newmap.get(day);
            log.info("기존값: " + existScore + "입력값: " + score);
            if (score > existScore) {
                newmap.put(day, score);
                member.updateScore((HashMap<String, Integer>) newmap);
            }
            Member updatedUser = memberRepository.save(member);
            log.info("변경된 값: " + updatedUser);
            return new DailyQuizResponseDto(updatedUser);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            log.error("예외 발생: " + e.getMessage(), e);
            throw new LearnExceptionHandler("User not found");
        }
    }
}
