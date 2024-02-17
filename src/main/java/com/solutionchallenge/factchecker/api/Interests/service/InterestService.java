package com.solutionchallenge.factchecker.api.Interests.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solutionchallenge.factchecker.api.Interests.dto.request.SelectedInterestsRequestDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.InterestArticleResponseDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.InterestResponseDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.MLResponseDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.SelectedInterestsResponseDto;
import com.solutionchallenge.factchecker.api.Interests.entity.Interest;
import com.solutionchallenge.factchecker.api.Interests.repository.InterestRepository;
import com.solutionchallenge.factchecker.api.Member.entity.Member;
import com.solutionchallenge.factchecker.api.Member.repository.MemberRepository;

import com.solutionchallenge.factchecker.global.exception.CustomException;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterestService {
    private final MemberRepository memberRepository;
    private final InterestRepository interestRepository;
    private final WebClient webClient;

    private static final Logger log = LoggerFactory.getLogger(InterestService.class);


    @Autowired
    public InterestService(MemberRepository memberRepository, InterestRepository interestRepository, WebClient.Builder webClientBuilder) {
        this.memberRepository = memberRepository;
        this.interestRepository = interestRepository;

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMinutes(5));

        this.webClient = webClientBuilder.baseUrl("http://34.22.87.117:5000")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    // 회원가입 시 선택한 키워드를 가져오는 메서드
    public List<String> getSelectedInterests(String memberId) {
        Optional<Member> memberOptional = memberRepository.findMemberById(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            // 사용자의 선택한 키워드가 저장된 Map에서 값들만 추출하여 List로 변환
            List<String> selectedInterests = new ArrayList<>(member.getInterests().values());
            return selectedInterests;
        } else {
            throw new RuntimeException("Member not found with id: " + memberId);
        }
    }

    // 클라이언트가 선택한 키워드를 저장하는 메서드
    @Transactional
    public SelectedInterestsResponseDto setSelectedInterests(String memberId, SelectedInterestsRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));

        // 새로운 선택된 관심 분야를 설정
        String updatedSelectedInterests = requestDto.getSelectedInterests().get(0);
        member.setSelectedInterests(updatedSelectedInterests);

        // 업데이트된 선택된 관심 분야를 응답 DTO에 담아서 반환
        return SelectedInterestsResponseDto.builder()
                .memberId(memberId)
                .selectedInterests(updatedSelectedInterests)
                .build();
    }

    private Mono<MLResponseDto> sendUrlToMlServer(String member_id, String url) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/interests")
                        .queryParam("url", url)
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        // 200번대 응답 처리
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    try {
                                        return handleMlServerResponse(member_id, url, body);
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
                    log.error("After retries or timeout, processing failed: {}", e.getMessage());
                    return Mono.error(new CustomException("재시도 횟수를 다 사용했습니다. ML 서버의 트래픽이 너무 많거나 응답 시간이 너무 깁니다. 재요청해주세요"));
                });
    }

    private Mono<MLResponseDto> handleMlServerResponse(String member_id, String url, String response) throws JsonProcessingException {
        return Mono.fromCallable(() -> {
            Member member = memberRepository.findMemberById(member_id).orElseThrow(() -> new CustomException("User not found"));

            Timestamp now = new Timestamp(System.currentTimeMillis());

            // JSON 응답을 MLResponseDto 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            MLResponseDto mlResponse = objectMapper.readValue(response, MLResponseDto.class);

            return mlResponse; // MLResponseDto 반환
        });
    }

    public List<InterestArticleResponseDto> getInterestArticles(String memberId) {
        // 회원 ID를 사용하여 선택한 키워드를 가져옵니다.
        List<String> selectedInterests = getSelectedInterests(memberId);

        List<InterestArticleResponseDto> interestArticleResponseDtos = new ArrayList<>();

        // 각 키워드에 대해 ML 서버로 요청을 보내어 기사 목록을 가져옵니다.
        for (String keyword : selectedInterests) {
            Mono<MLResponseDto> mlResponseMono = sendUrlToMlServer(memberId, keyword);
            mlResponseMono.subscribe(mlResponse -> {
                // ML 서버에서 가져온 기사 목록을 InterestArticleResponseDto로 변환하여 리스트에 추가합니다.
                InterestArticleResponseDto dto = new InterestArticleResponseDto();
                dto.setTitle(mlResponse.getTitle());
                dto.setArticle(mlResponse.getArticle());
                dto.setDate(mlResponse.getDate());
                dto.setCredibility(mlResponse.getCredibility());
                dto.setSection(mlResponse.getSection());
                interestArticleResponseDtos.add(dto);

                // 가져온 기사 목록을 interest_article 테이블에 저장합니다.
                saveInterestArticle(dto);
            });
        }

        return interestArticleResponseDtos;
    }

    public void saveInterestArticle(InterestArticleResponseDto interestArticleResponseDto) {
        Interest interest = new Interest();
        interest.setTitle(interestArticleResponseDto.getTitle());
        interest.setArticle(interestArticleResponseDto.getArticle());
        interest.setDate(interestArticleResponseDto.getDate());
        interest.setCredibility(interestArticleResponseDto.getCredibility());
        interest.setSection(interestArticleResponseDto.getSection());

        interestRepository.save(interest);
    }


//    @Transactional
//    public List<InterestArticleResponseDto> getInterestArticles() {
//        // ml 서버로부터 기사 목록을 가져오는 요청
//        ResponseEntity<InterestArticleResponseDto[]> responseEntity = webClient.get()
//                .uri("/interests")
//                .retrieve()
//                .toEntity(InterestArticleResponseDto[].class)
//                .block();
//
//        // 가져온 기사 목록을 배열에서 리스트로 변환하여 반환
//        if (responseEntity != null && responseEntity.getBody() != null) {
//            return Arrays.asList(responseEntity.getBody());
//        } else {
//            throw new RuntimeException("Failed to retrieve interest articles.");
//        }
//    }

}
