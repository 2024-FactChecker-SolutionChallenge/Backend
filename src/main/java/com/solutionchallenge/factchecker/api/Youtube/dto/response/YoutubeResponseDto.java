package com.solutionchallenge.factchecker.api.Youtube.dto.response;

import com.solutionchallenge.factchecker.api.Member.entity.Member;
import com.solutionchallenge.factchecker.api.Youtube.entity.Youtube;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YoutubeResponseDto {
    private Long id;
    private String url;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String memberId;

    public YoutubeResponseDto(Youtube youtube) {
        this.id = youtube.getId();
        this.url = youtube.getUrl();
        this.createdDate = youtube.getCreatedDate();
        this.modifiedDate = youtube.getModifiedDate();
        this.memberId = youtube.getMember().getId();
    }
}
