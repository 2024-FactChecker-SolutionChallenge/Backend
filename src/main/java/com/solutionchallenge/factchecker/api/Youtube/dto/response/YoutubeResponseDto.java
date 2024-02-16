package com.solutionchallenge.factchecker.api.Youtube.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YoutubeResponseDto {
    private Long id;
    private String title;
    private String url;
    private List<RelatedNewsDto> relatedNews;

    public YoutubeResponseDto(Long id, String title, String url, List<RelatedNewsDto> relatedNews) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.relatedNews = relatedNews;
    }
}
