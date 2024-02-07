package com.solutionchallenge.factchecker.Member.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class MemberLoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long id;
    private String nickname;
    private String email;
    private String grade;
    private Map<String, String> interests;

    public MemberLoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public MemberLoginResponse(
            Long id, String nickname, String email, String grade,
            Map<String, String> interests, String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.grade = grade;
        this.interests = interests;
    }
}
