package com.solutionchallenge.factchecker.Member.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class InterestRequest {
    private Map<String, String> interview_time;
}