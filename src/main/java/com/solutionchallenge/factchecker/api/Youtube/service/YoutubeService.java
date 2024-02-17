package com.solutionchallenge.factchecker.api.Youtube.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solutionchallenge.factchecker.api.Member.entity.Member;
import com.solutionchallenge.factchecker.api.Member.repository.MemberRepository;
import com.solutionchallenge.factchecker.api.Youtube.dto.response.*;
import com.solutionchallenge.factchecker.api.Youtube.entity.*;
import com.solutionchallenge.factchecker.api.Youtube.repository.*;
import com.solutionchallenge.factchecker.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class YoutubeService {
    private final YoutubeRepository youtubeRepository;
    private final MemberRepository memberRepository;
    private final RelatedNewsRepository relatedNewsRepository;
    private final WebClient webClient;
    @Autowired
    public YoutubeService(YoutubeRepository youtubeRepository, MemberRepository memberRepository, RelatedNewsRepository relatedNewsRepository, WebClient.Builder webClientBuilder) {
        this.youtubeRepository = youtubeRepository;
        this.memberRepository = memberRepository;
        this.relatedNewsRepository = relatedNewsRepository;
        // HttpClient를 사용하여 타임아웃 설정
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMinutes(5)); // 5분 타임아웃 설정

        this.webClient = webClientBuilder.baseUrl("http://34.22.87.117:5000")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public YoutubeSuccessDto processYoutubeNews(String member_id, String url) {
        Optional<Youtube> existingYoutube = youtubeRepository.findByUrl(url);
        if (existingYoutube.isPresent()) {
            throw new CustomException("url already processed.");
        }
        Member member = memberRepository.findMemberById(member_id).orElseThrow(() -> new CustomException("User not found"));
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Youtube youtube = Youtube.builder()
                .createdDate(now)
                .modifiedDate(now)
                .member(member)
                .url(url)
                .build();

        Youtube savedyoutube = youtubeRepository.save(youtube);

        // ML 서버에 URL 처리 요청
        sendUrlToMlServer(savedyoutube, savedyoutube.getUrl()).block();
        return new YoutubeSuccessDto(member_id, url);
    }


    private Mono<MLResponseDto> sendUrlToMlServer(Youtube youtube, String url) {
        return webClient.post()
                .uri("/youtubeNews/related")
                .bodyValue(Collections.singletonMap("url", url))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        // 200번대 응답 처리
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    try {
                                        return handleMlServerResponse(youtube, body);
                                    } catch (JsonProcessingException e) {
                                        return Mono.error(new RuntimeException(e));
                                    }
                                });
                    } else if (response.statusCode().is4xxClientError()) {
                        // 400번대 에러 처리, 예외 발생
                        return Mono.error(new CustomException("Client error, incorrect request"));
                    } else if (response.statusCode().is5xxServerError()) {
                        // 500번대 에러 처리, 재시도를 위해 에러 발생
                        return Mono.error(new CustomException("Server error, retrying..."));
                    } else {
                        // 그 외 상태 코드 처리, 예외 발생
                        return Mono.error(new CustomException("Unexpected response status: " + response.statusCode()));
                    }
                })
                .retryWhen(Retry.max(5)
                        .filter(throwable -> throwable instanceof CustomException && throwable.getMessage().contains("retrying")))
                .onErrorResume(e -> {
                    if (e instanceof CustomException && (e.getMessage().contains("Client error") || e.getMessage().contains("retrying"))) {
                        youtubeRepository.deleteByUrlAndMember(url,youtube.getMember()); // 비동기 삭제 실행
                    }
                    log.error("After retries or timeout, processing failed: {}", e.getMessage());
                    return Mono.error(new CustomException("재시도 횟수를 다 사용했습니다. ML 서버의 트래픽이 너무 많거나 응답 시간이 너무 깁니다. 재요청해주세요"));
                });
    }


    private Mono<MLResponseDto> handleMlServerResponse(Youtube youtube, String response) throws JsonProcessingException {
        return Mono.fromCallable(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            MLResponseDto mlResponse = objectMapper.readValue(response, MLResponseDto.class);

            List<RelatedNewsDto> currYoutubeNews = mlResponse.getCurrYoutubeNews();
            List<RelatedNewsDto> relYoutubeNews = mlResponse.getRelYoutubeNews();
            String title = mlResponse.getTitle();
            String keyword = mlResponse.getKeyword();
            String upload_date = mlResponse.getUploadDate();

            Youtube myyoutube = youtubeRepository.findByIdAndMember(youtube.getId(),youtube.getMember() )
                    .orElseThrow(() -> new CustomException("YoutubeNews not found: " + youtube.getId()));
            myyoutube.updateKeywordAndUploadDate(title, keyword, upload_date);
            Youtube updatedyoutube = youtubeRepository.save(myyoutube);
            currYoutubeNews.forEach(newsDto -> saveRelatedNews(newsDto, Category.LATEST, updatedyoutube));
            relYoutubeNews.forEach(newsDto -> saveRelatedNews(newsDto, Category.RELATED, updatedyoutube));

            return mlResponse; // MLResponseDto 반환
        });
    }


    public void saveRelatedNews(RelatedNewsDto newsDto, Category category, Youtube youtube) {
        Member member = youtube.getMember();

        RelatedNews relatedNews = RelatedNews.builder()
                .title(newsDto.getTitle())
                .article(newsDto.getArticle())
                .upload_date(newsDto.getDate())
                .credibility(newsDto.getCredibility())
                .category(category)
                .youtube(youtube)
                .member(member)
                .build();

        relatedNewsRepository.save(relatedNews);
    }


    public List<YoutubeResponseDto> fetchYoutubeNews(String member_id) {
        List<Youtube> youtubes = youtubeRepository.findAllByMember_Id(member_id);
        if (youtubes.isEmpty()) {
            throw new CustomException("No Youtube news found for member ID: " + member_id);
        }

        return youtubes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public YoutubeResponseDto convertToDto(Youtube youtube) {
        List<RelatedNewsDto> relatedNewsDtos = youtube.getRelatedNews().stream()
                .map(news -> new RelatedNewsDto(news.getId(), news.getTitle(), news.getArticle(), news.getUpload_date(), news.getCredibility(),news.getCategory()))
                .collect(Collectors.toList());
        log.info("[youtube  타이틀은 다음과 같다: ", youtube.getTitle());
        return new YoutubeResponseDto(youtube.getId(), youtube.getTitle(), youtube.getUrl(), relatedNewsDtos);
    }



    public ArticleDetailDto getArticleDetail(Long articleId, String memberId) {
        Optional<RelatedNews> relatedNewsOptional = relatedNewsRepository.findById(articleId);
        if (relatedNewsOptional.isEmpty()) {
            throw new CustomException("Related news not found with ID: " + articleId);
        }

        RelatedNews relatedNews = relatedNewsOptional.get();
        if (!relatedNews.getYoutube().getMember().getId().equals(memberId)) {
            throw new CustomException("You are not authorized to access this article.");
        }

        return new ArticleDetailDto(relatedNews.getTitle(), relatedNews.getArticle());
    }




}
