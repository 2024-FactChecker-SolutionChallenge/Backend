package com.solutionchallenge.factchecker.api.Interests.controller;

import com.solutionchallenge.factchecker.api.Interests.dto.request.SelectedInterestsRequestDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.InterestArticleResponseDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.SelectedInterestsResponseDto;
import com.solutionchallenge.factchecker.api.Interests.service.InterestService;
import com.solutionchallenge.factchecker.api.Member.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name= "Interest Controller ", description = "관심기사 API")
@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;

    @GetMapping("/selected/{memberId}")
    public ResponseEntity<List<String>> getSelectedInterests(@PathVariable String memberId) {
        List<String> selectedInterests = interestService.getSelectedInterests(memberId);
        return ResponseEntity.ok(selectedInterests);
    }

    @PostMapping("/selected/{memberId}")
    public ResponseEntity<SelectedInterestsResponseDto> setSelectedInterests(
            @PathVariable String memberId,
            @RequestBody SelectedInterestsRequestDto requestDto) {
        SelectedInterestsResponseDto responseDto = interestService.setSelectedInterests(memberId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @Operation(summary = "홈(메인) 화면 - ML 서버 값 DB저장 ", description = "ml서버에 요청을 보내고 받은 응답을 DB에 저장합니다.(차후 한시간에 한번씩 자동으로 실행되도록 변경 필요)",
            responses = {@ApiResponse(responseCode = "200", description = "관심기사 목록이 성공적으로 DB에 저장되었습니다.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InterestArticleResponseDto.class))))
            })
    @GetMapping("/getarticle")
    public ResponseEntity<List<InterestArticleResponseDto>> getAllInterestArticles(
            @RequestHeader(name = "ACCESS_TOKEN", required = false) String ACCESS_TOKEN,
            @RequestHeader(name = "REFRESH_TOKEN", required = false) String REFRESH_TOKEN) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;

        List<InterestArticleResponseDto> dtos = interestService.getArticles(userDetails.getUsername());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/getarticleBySection")
    public ResponseEntity<List<InterestArticleResponseDto>> getInterestArticlesBySection(
            @RequestParam(name = "section") int section,
            @RequestHeader(name = "ACCESS_TOKEN", required = false) String ACCESS_TOKEN,
            @RequestHeader(name = "REFRESH_TOKEN", required = false) String REFRESH_TOKEN) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;

        List<InterestArticleResponseDto> dtos = interestService.getArticlesBySection(userDetails.getUsername(), section);
        return ResponseEntity.ok(dtos);
    }

}
