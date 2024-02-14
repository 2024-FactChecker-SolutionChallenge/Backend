package com.solutionchallenge.factchecker.api.Youtube.controller;

import com.solutionchallenge.factchecker.api.Learn.dto.response.DailyQuizScoreResponseDto;
import com.solutionchallenge.factchecker.api.Member.entity.UserDetailsImpl;
import com.solutionchallenge.factchecker.api.Youtube.dto.request.YoutubeURLRequestDto;
import com.solutionchallenge.factchecker.api.Youtube.dto.response.YoutubeResponseDto;
import com.solutionchallenge.factchecker.api.Youtube.service.YoutubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name= "Youtube Controller", description = "유튜브영상뉴스 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class YoutubeController {
    private final YoutubeService youtubeService;

    @Operation(summary = "유튜브 영상뉴스 - 유튜브 url 입력", description = "유튜브 url을 입력받는다.",
            responses = {@ApiResponse(responseCode = "200", description = "유튜브주소가 성공적으로 저장되었습니다.",
                    content = @Content(schema = @Schema(implementation = DailyQuizScoreResponseDto.class)))
            })
    @PostMapping("/youtubeNews/add")
    public ResponseEntity<?> addYoutubeNewsURl(
            @RequestBody YoutubeURLRequestDto youtubeURLRequestDto,
            @RequestHeader(name = "ACCESS_TOKEN", required = false) String ACCESS_TOKEN,
            @RequestHeader(name = "REFRESH_TOKEN", required = false) String REFRESH_TOKEN
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;

        YoutubeResponseDto dto = youtubeService.saveYoutubeNews(userDetails.getUsername(), youtubeURLRequestDto.getUrl());
        return ResponseEntity.ok().body(dto);
    }


}
