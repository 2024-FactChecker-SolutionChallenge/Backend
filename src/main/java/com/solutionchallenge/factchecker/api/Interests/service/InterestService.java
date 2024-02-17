package com.solutionchallenge.factchecker.api.Interests.service;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.core.ParameterizedTypeReference;
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
    public List<InterestArticleResponseDto> getArticles(String memberId) {
        //1. ML에서 전체 기사 데이터 받아오기 MLResponseDTO로 매핑해서 가지고 있기./ DTo를 Entity로 매핑해서 DB에 저장하기
        saveInterestsFromMLResponse();
        // DB에서 모든 Interest 엔티티를 조회
        List<Interest> interests = interestRepository.findAll();

        // 조회된 Interest 엔티티 리스트를 InterestArticleResponseDto 리스트로 변환
        List<InterestArticleResponseDto> dtos = interests.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
        //FE로 반환
        return dtos;

    }

    @Transactional
    public void saveInterestsFromMLResponse() {
        // fetchDataFromMLServer()가 Mono<List<MLResponseDto>> 대신 List<MLResponseDto>를 반환한다고 가정
        List<MLResponseDto> dtos = fetchDataFromMLServer().block(); // 리액티브 타입을 동기적으로 처리

        for (MLResponseDto dto : dtos) {
            Interest interest = convertDtoToEntity(dto);
            interestRepository.save(interest); // 변환된 엔티티를 데이터베이스에 저장
        }
    }

    private Mono<List<MLResponseDto>> fetchDataFromMLServer() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/interests").build())
                .retrieve() // exchangeToMono 대신 retrieve 사용
                .bodyToMono(new ParameterizedTypeReference<List<MLResponseDto>>() {});
    }
    private Interest convertDtoToEntity(MLResponseDto dto) {
        Interest interest = new Interest();
        interest.setTitle(dto.getTitle());
        interest.setArticle(dto.getArticle());
        interest.setDate(dto.getDate());
        interest.setCredibility(dto.getCredibility());
        interest.setSection(dto.getSection());

        return interest;
    }

    private InterestArticleResponseDto convertEntityToDto(Interest interest) {
        // Interest 엔티티를 InterestArticleResponseDto로 변환하는 로직
        InterestArticleResponseDto dto = new InterestArticleResponseDto();
        dto.setId(interest.getId());
        dto.setTitle(interest.getTitle());
        dto.setArticle(interest.getArticle());
        dto.setDate(interest.getDate());
        dto.setCredibility(interest.getCredibility());
        dto.setSection(interest.getSection());
        return dto;
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
