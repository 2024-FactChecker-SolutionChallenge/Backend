package com.solutionchallenge.factchecker.api.Interests.controller;

import com.solutionchallenge.factchecker.api.Interests.dto.request.SelectedInterestsRequestDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.InterestArticleResponseDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.SelectedInterestsResponseDto;
import com.solutionchallenge.factchecker.api.Interests.entity.Interest;
import com.solutionchallenge.factchecker.api.Interests.service.InterestService;
import com.solutionchallenge.factchecker.api.Member.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @GetMapping("/getarticle")
    public ResponseEntity<List<InterestArticleResponseDto>> getInterestArticles(
            @RequestHeader(name = "ACCESS_TOKEN", required = false) String ACCESS_TOKEN,
            @RequestHeader(name = "REFRESH_TOKEN", required = false) String REFRESH_TOKEN) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;


        List<InterestArticleResponseDto> dtos = interestService.getInterestArticles(userDetails.getUsername());
        return ResponseEntity.ok(dtos);
    }

}
