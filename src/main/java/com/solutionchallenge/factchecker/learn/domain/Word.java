package com.solutionchallenge.factchecker.learn.domain;

import com.sun.xml.bind.v2.TODO;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.function.ToDoubleBiFunction;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wordId", unique = true, nullable = false)
    private Long wordId;

    @Column(name = "word")
    @NotNull
    private String word;

    @Column(name = "mean")
    private String mean;

    @Column(name= "knowStatus")
    @NotNull
    private boolean knowStatus;

    @Column(name = "createdDate")
    @NotNull
    private Timestamp createdDate;

    @Column(name = "modifiedDate")
    @NotNull
    private Timestamp modifiedDate;

    // User 엔터티에 대한 참조 (외래 키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @NotNull
    private User user;

    @Builder
    public Word(Long wordId, String word, String mean, boolean knowStatus, Timestamp createdDate, Timestamp modifiedDate) {
        this.wordId = wordId;
        this.word = word;
        this.mean = mean;
        this.knowStatus = knowStatus;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public void updateWord(String word, String mean, boolean knowStatus) {
        this.word= word;
        this.mean= mean;
        this.knowStatus = knowStatus;
        // TODO : modified 날짜도 updateWord를 호출한 당시로 갱신해준다.

    }
}
