package com.solutionchallenge.factchecker.learn.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizWordRepository  extends JpaRepository<QuizWord, Long> {
    // 네이티브 쿼리를 사용하여 랜덤으로 4개의 단어를 가져오는 메서드
    @Query(value = "SELECT * FROM quiz_word ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<QuizWord> findRandomFourWords();
}
