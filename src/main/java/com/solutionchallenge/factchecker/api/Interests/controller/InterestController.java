package com.solutionchallenge.factchecker.api.Interests.controller;

import com.solutionchallenge.factchecker.api.Interests.dto.request.SelectedInterestsRequestDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.InterestResponseDto;
import com.solutionchallenge.factchecker.api.Interests.dto.response.SelectedInterestsResponseDto;
import com.solutionchallenge.factchecker.api.Interests.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;

    // 클라이언트에게 회원가입 시 선택한 키워드 목록을 제공
    // 클라이언트에게 회원가입 시 선택한 키워드 목록을 제공
    @GetMapping("/selected/{memberId}")
    public ResponseEntity<List<String>> getSelectedInterests(@PathVariable String memberId) {
        List<String> selectedInterests = interestService.getSelectedInterests(memberId);
        return ResponseEntity.ok(selectedInterests);
    }

    // 클라이언트가 메인화면에서 3가지 관심분야 중 선택한 키워드 반환
    @PostMapping("/selected/{memberId}")
    public ResponseEntity<SelectedInterestsResponseDto> setSelectedInterests(
            @PathVariable String memberId,
            @RequestBody SelectedInterestsRequestDto requestDto) {
        SelectedInterestsResponseDto responseDto = interestService.setSelectedInterests(memberId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}

