package com.solutionchallenge.factchecker.api.Member.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data

public class LoginResponseDto {

    String id;
    String nickname;


    @Builder
    public LoginResponseDto(
            String id, String nickname) {
        this.id = id;
        this.nickname = nickname;


    }
}