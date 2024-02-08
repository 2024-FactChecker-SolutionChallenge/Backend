package com.solutionchallenge.factchecker.learn.controller;

import com.solutionchallenge.factchecker.learn.dto.response.DailyQuizResponseDto;
import com.solutionchallenge.factchecker.learn.dto.response.QuizWordResponseDto;
import com.solutionchallenge.factchecker.learn.dto.response.WordResponseDto;
import com.solutionchallenge.factchecker.learn.dto.request.DailyQuizRequestDto;
import com.solutionchallenge.factchecker.learn.service.LearnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.List;

@Tag(name= "Learn Controller", description = "단어퀴즈 페이지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LearnController {
    private final LearnService learnService;

    @Operation(summary = "단어장 - 단어 리스트 조회", description = "단어장에 올릴 단어들을 조회합니다. 이때 생성일 기준 최신순으로 정렬해서 가져옵니다.", tags = { "Word Controller" },
            responses = {@ApiResponse(responseCode = "200", description = "조회 성공")})
    @GetMapping("/words")
    public ResponseEntity<?> returnWords(@RequestHeader String Authorization){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        List<WordResponseDto> list = learnService.getWordList(principal.getId());
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "단어장 - 단어 knowstatus 정보 수정", description = "단어장에서 토글버튼을 누르면 받아온 값에 따라 true-> false/ false -> true 전환", tags = { "Word Controller" },
            responses = {@ApiResponse(responseCode = "200", description = "조회 성공")})
    @PatchMapping("/wordlist/{wordId}")
    public ResponseEntity<?> setWordKnowStatus(@PathVariable Long wordId, @RequestHeader String Authorization) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        WordResponseDto dto = learnService.updateWord(wordId, principal.getId());
        return ResponseEntity.ok().body(dto);
    }
    //랜덤이므로 querydsl 사용예정- 랜덤으로 knowstatus false인 단어 리스트로 가져오기
    @Operation(summary = "어휘학습 - flip card", description = "모르는 단어를 랜덤으로 가져온다 -> flip card로 제공", tags = { "Word Controller"},
            responses = {@ApiResponse(responseCode = "200", description = "조회 성공")})
    @GetMapping("/study/flip-cards/word")
    public ResponseEntity<?> returnFlipCardWords(@RequestParam Long userId, @RequestHeader String Authorization){
        List<WordResponseDto> list  = learnService.getUnknownWordList(userId);
        return ResponseEntity.ok().body(list);
    }
    //랜덤이므로 querydsl 사용예정- 랜덤 단어 4개 뽑아오기
    @Operation(summary = "데일리 퀴즈", description = "퀴즈 -  랜덤퀴즈단어 4개 조회", tags = { "Word Controller" },
            responses = {@ApiResponse(responseCode = "200", description = "조회 성공")}
    )
    @GetMapping("/api/study/daily-quiz/word")
    public ResponseEntity<?> returnFlipCard(@RequestHeader String Authorization
    ){
        List<QuizWordResponseDto> list  = learnService.getQuiz();
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "데일리 퀴즈 - 퀴즈점수 저장", description = "퀴즈 점수를 저장한다.", tags = { "Word Controller" },
            responses = {@ApiResponse(responseCode = "200", description = "일일 점수가 성공적으로 저장되었습니다.")}
    )
    @PostMapping("/api/study/daily-quiz/score")
    public ResponseEntity<?> setScore(
            @RequestBody DailyQuizRequestDto dailyQuizRequestDto,
            @RequestHeader String Authorization
    ){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        DailyQuizResponseDto dto = learnService.updateScore(principal.getId(), dailyQuizRequestDto.getDay(),dailyQuizRequestDto.getScore());
        return ResponseEntity.ok().body(dto);
    }

}
