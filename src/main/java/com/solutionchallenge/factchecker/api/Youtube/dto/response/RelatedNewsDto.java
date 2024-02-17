package com.solutionchallenge.factchecker.api.Youtube.dto.response;


import com.solutionchallenge.factchecker.api.Youtube.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RelatedNewsDto {
    private Long id;
    private String title;
    private String article;
    private String date;
    private float credibility;
    private Category category;

    public RelatedNewsDto(Long id, String title, String article, String date, float credibility, Category category) {
        this.id = id;
        this.title = title;
        this.article = article;
        this.date = date;
        this.credibility = credibility;
        this.category = category;
    }
}
