package com.solutionchallenge.factchecker.api.Interests.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class SelectedInterestsResponseDto {
    private String memberId;
    private String selectedInterests;

}