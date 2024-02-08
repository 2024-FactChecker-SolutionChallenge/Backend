package com.solutionchallenge.factchecker.learn.dto.request;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
public class WordRequestDto{
    private Long wordId;
    private boolean knowStatus;

    public WordRequestDto(Long wordId, boolean knowStatus) {
        this.wordId = wordId;
        this.knowStatus = knowStatus;
    }

}
