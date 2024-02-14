package com.solutionchallenge.factchecker.api.Youtube.service;



import com.solutionchallenge.factchecker.api.Member.entity.Member;
import com.solutionchallenge.factchecker.api.Member.repository.MemberRepository;
import com.solutionchallenge.factchecker.api.Youtube.dto.response.YoutubeResponseDto;
import com.solutionchallenge.factchecker.api.Youtube.entity.Youtube;
import com.solutionchallenge.factchecker.api.Youtube.repository.YoutubeRepository;
import com.solutionchallenge.factchecker.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class YoutubeService{
    private final YoutubeRepository youtubeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public YoutubeService(YoutubeRepository youtubeRepository, MemberRepository memberRepository) {
        this.youtubeRepository = youtubeRepository;
        this.memberRepository = memberRepository;
    }

    public YoutubeResponseDto saveYoutubeNews(String member_id, String url) {

        Member member = memberRepository.findMemberById(member_id).orElseThrow(()->new CustomException("User not found"));

        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Youtube 엔터티 인스턴스 생성
        Youtube youtube = Youtube.builder()
                .createdDate(now)
                .modifiedDate(now)
                .member(member)
                .url(url) // Youtube 클래스에 url 필드가 있다고 가정할 때
                .build();

        // Youtube 엔터티 저장
        Youtube savedyoutube = youtubeRepository.save(youtube);
        return new YoutubeResponseDto(savedyoutube);
    }
}
