package com.solutionchallenge.factchecker.Member.service;

import com.solutionchallenge.factchecker.Member.dto.request.MemberSignupRequest;
import com.solutionchallenge.factchecker.Member.dto.response.MemberSignupResponse;
import com.solutionchallenge.factchecker.Member.entity.Grade;
import com.solutionchallenge.factchecker.Member.entity.Member;
import com.solutionchallenge.factchecker.Member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MemberService {

    @Autowired
    private MemberRepository userRepository;

    public MemberSignupResponse registerSignup(MemberSignupRequest request) {
        Grade grade = validateGrade(request.getGrade());
        Map<String, String> interests = request.getInterests();

        Member user = userRepository.save(
                Member.builder()
                        .nickname(request.getNickname())
                        .email(request.getEmail())
                        .emailVerificationCode(request.getEmailVerificationCode())
                        .password(request.getPassword())
                        .grade(grade)
                        .interests(interests)
                        .build()
        );

        return userToResponseDto(user);
    }

    private Grade validateGrade(String grade) {
        return Grade.getGrade(grade);
    }

    private MemberSignupResponse userToResponseDto(Member user) {
        String nickname = (user.getNickname() != null) ? user.getNickname() : "";
        String email = (user.getEmail() != null) ? user.getEmail() : "";
        String userGrade = (user.getGrade() != null) ? user.getGrade().getGrade() : "";
        Map<String, String> userInterests = (user.getInterests() != null) ? user.getInterests() : Map.of();

        return new MemberSignupResponse(nickname, email, userGrade, userInterests);
    }
}
