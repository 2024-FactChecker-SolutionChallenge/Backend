package com.solutionchallenge.factchecker.learn.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "word")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quiz_wordId;

    @Column(nullable = false)
    private String quiz_word;

    private String mean;

    private Timestamp createdDate;

}
