package com.solutionchallenge.factchecker.learn.dto.response;

import com.solutionchallenge.factchecker.learn.domain.Word;
import lombok.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WordResponseDto {
    private Long id;
    private String word;
    private String mean;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private Boolean knowStatus;

    public WordResponseDto(Word word) {

    }
}
