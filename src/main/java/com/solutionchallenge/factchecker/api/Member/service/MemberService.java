package com.solutionchallenge.factchecker.api.Member.service;
import com.solutionchallenge.factchecker.api.Member.dto.request.LoginRequestDto;
import com.solutionchallenge.factchecker.api.Member.dto.request.SignupRequestDto;
import com.solutionchallenge.factchecker.api.Member.dto.response.LoginResponseDto;
 import com.solutionchallenge.factchecker.api.Member.dto.response.TokenResponseDto;
import com.solutionchallenge.factchecker.api.Member.entity.Grade;
import com.solutionchallenge.factchecker.api.Member.entity.Member;
import com.solutionchallenge.factchecker.api.Member.repository.MemberRepository;
import com.solutionchallenge.factchecker.global.auth.JwtUtil;
import com.solutionchallenge.factchecker.global.entity.RefreshToken;
import com.solutionchallenge.factchecker.global.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /*
     ** 회원가입
     */
    @Transactional
    public Member signup(HttpServletResponse res, SignupRequestDto signupRequestDto) throws Exception {

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        Grade grade = Grade.getGrade(signupRequestDto.getGrade());

        Member member = Member.builder()
                .id(signupRequestDto.getId())
                .nickname(signupRequestDto.getNickname())
                .password(encodedPassword)
                .grade(grade)
                .interests(signupRequestDto.getInterests())
                .build();
        memberRepository.save(member);

        // 토큰 생성
        TokenResponseDto tokenResponseDto = jwtUtil.createAllToken(signupRequestDto.getId()); // 토큰 생성

        // Refresh Token DB 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .email(signupRequestDto.getId())
                .token(tokenResponseDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        jwtUtil.setHeaderToken(res, tokenResponseDto);
        return member;
    }

    /*
     ** 로그인
     */
    @Transactional
    public LoginResponseDto login(HttpServletResponse res, LoginRequestDto loginRequestDto) throws Exception {
        // 존재하지 않는 회원에 대한 에러처리
        if (!memberRepository.existsMemberById(loginRequestDto.getId())) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new BadCredentialsException("존재하지 않는 이메일입니다.");
        }

        else {
            Member member = memberRepository.findMemberById(loginRequestDto.getId());

            if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }

            TokenResponseDto tokenResponseDto = jwtUtil.createAllToken(member.getId());
            jwtUtil.setHeaderToken(res, tokenResponseDto);

            log.info("[로그인 알림] {} 회원님이 로그인했습니다.", member.getId());




            return LoginResponseDto.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .build();
        }
    }

    /* */

}
