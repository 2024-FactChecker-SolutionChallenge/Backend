package com.solutionchallenge.factchecker.api.Youtube.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YoutubeResponseDto {
    private Long id;
    private String title;
    private String url;
    private final boolean show= false;
    private final boolean loadingStatus=false;
    private List<RelatedNewsDto> relatedNews;

    public YoutubeResponseDto(Long id, String title, String url, List<RelatedNewsDto> relatedNews) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.relatedNews = relatedNews;
    }
}
