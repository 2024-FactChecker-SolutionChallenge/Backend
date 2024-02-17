package com.solutionchallenge.factchecker.api.Interests.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solutionchallenge.factchecker.api.Youtube.dto.response.RelatedNewsDto;
import com.solutionchallenge.factchecker.api.Youtube.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class InterestArticleResponseDto {
    private Long id;
    private String title;
    private String article;
    private String date;
    private int section;
    private float credibility;


    public InterestArticleResponseDto(Long id, String title, String article, String date, int section, float credibility) {
        this.id = id;
        this.title = title;
        this.article = article;
        this.date = date;
        this.section = section;
        this.credibility = credibility;
    }



}
