package com.solutionchallenge.factchecker.Member.dto.request;

import com.solutionchallenge.factchecker.Member.entity.Grade;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

public class MemberSignupRequest {

    @NotBlank(message = "닉네임을 입력해야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일을 입력해야 합니다.")
    private String email;

    @NotBlank(message = "인증번호를 입력해야 합니다.")
    private String emailVerificationCode;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String password;

    @NotBlank(message = "난이도를 선택해야 합니다.")
    private String grade;

    @Valid
    @NotNull
    @Size(min = 1, max = 3)
    private Map<@NotBlank(message = "관심 분야를 선택해야 합니다.") String, String> interests;

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

    public String getEmailVerificationCode() {
        return emailVerificationCode;
    }

    public void setEmailVerificationCode(String emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    private Grade convertToGradeEnum(String grade) {
        try {
            return Grade.valueOf(grade.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Grade.BEGINNER;
        }
    }
}
