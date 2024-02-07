package com.solutionchallenge.factchecker.Member.dto.response;

import java.util.Map;

public class MemberSignupResponse {

    private Long userId;  // userId 추가
    private String nickname;
    private String email;
    private String grade;
    private Map<String, String> interests;

    public MemberSignupResponse() {
    }

    public MemberSignupResponse(Long userId, String nickname, String email, String grade, Map<String, String> interests) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.grade = grade;
        this.interests = interests;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Map<String, String> getInterests() {
        return interests;
    }

    public void setInterests(Map<String, String> interests) {
        this.interests = interests;
    }
}
