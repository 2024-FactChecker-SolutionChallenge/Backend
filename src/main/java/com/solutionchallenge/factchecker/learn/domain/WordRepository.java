package com.solutionchallenge.factchecker.learn.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository  extends JpaRepository<Word, Long> {
    Optional<Word> findByIdAndUser_WordId(Long wordId, Long userId);
    List<Word> findAllByUserId(Long userId);

    List<Word> findByUserIdAndKnowStatus(Long userId,  boolean knowStatus);
}
