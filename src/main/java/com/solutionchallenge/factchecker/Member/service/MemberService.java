package com.solutionchallenge.factchecker.Member.service;
import com.solutionchallenge.factchecker.Member.dto.request.MemberSignupRequest;
import com.solutionchallenge.factchecker.Member.dto.request.TokenInfo;
import com.solutionchallenge.factchecker.Member.dto.response.MemberSignupResponse;
import com.solutionchallenge.factchecker.Member.dto.response.TokenResponse;
import com.solutionchallenge.factchecker.Member.entity.Grade;
import com.solutionchallenge.factchecker.Member.entity.Member;
import com.solutionchallenge.factchecker.Member.repository.MemberRepository;
import com.solutionchallenge.factchecker.global.auth.JwtTokenProvider;
import com.solutionchallenge.factchecker.global.entity.RefreshToken;
import com.solutionchallenge.factchecker.global.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@Slf4j
public class MemberService  {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         RefreshTokenRepository refreshTokenRepository,
                         JwtTokenProvider jwtUtil,
                         PasswordEncoder passwordEncoder,
                         AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public class RegistrationException extends RuntimeException {
        public RegistrationException(String message) {
            super(message);
        }

        public RegistrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Transactional
    public MemberSignupResponse registerSignup(MemberSignupRequest signupRequest) {
        try {
            Grade grade = validateGrade(signupRequest.getGrade());
            Map<String, String> interests = signupRequest.getInterests();
            String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

            Member member = Member.builder()
                    .nickname(signupRequest.getNickname())
                    .email(signupRequest.getEmail())
                    .password(encodedPassword)
                    .grade(grade)
                    .interests(interests)
                    .build();
            memberRepository.save(member);

            TokenResponse tokenResponseDto = jwtUtil.createAllToken(signupRequest.getEmail());
            RefreshToken refreshToken = RefreshToken.builder()
                    .email(signupRequest.getEmail())
                    .token(tokenResponseDto.getRefreshToken())
                    .build();
            refreshTokenRepository.save(refreshToken);

            return userToResponseDto(member);
        } catch (Exception e) {
            log.error("Error occurred during user registration", e);
            throw new RegistrationException("Error occurred during user registration", e);
        }
    }


    @Transactional
    public TokenInfo login(String email, String password) {
        try {
            if (email == null || password == null) {
                throw new IllegalArgumentException("Email and password must not be null");
            }

            log.debug("Attempting to authenticate user with email: {}", email);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

            if (authenticationToken == null) {
                throw new IllegalArgumentException("Authentication token must not be null");
            }

            Authentication authentication = authenticateUser(authenticationToken);

            if (authentication == null) {
                throw new AuthenticationServiceException("Authentication object must not be null");
            }

            TokenInfo tokenInfo = jwtUtil.generateToken(authentication);
            if (tokenInfo == null) {
                throw new AuthenticationServiceException("TokenInfo object must not be null");
            }

            log.debug("JWT token generated successfully.");

            return tokenInfo;
        } catch (AuthenticationServiceException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Invalid arguments: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred during login process: {}", e.getMessage());
            throw new AuthenticationServiceException("An unexpected error occurred during login process.", e);
        }
    }


    private Authentication authenticateUser(UsernamePasswordAuthenticationToken authenticationToken) {
        if (authenticationToken == null) {
            throw new IllegalArgumentException("Authentication token must not be null");
        }
        log.debug("UsernamePasswordAuthenticationToken created.");

        // authenticationToken이 널이 아닌 경우에만 인증을 시도합니다.
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }



    private Grade validateGrade(String grade) {
        return Grade.getGrade(grade);
    }

    private MemberSignupResponse userToResponseDto(Member user) {
        String nickname = user.getNickname() != null ? user.getNickname() : "";
        String email = user.getEmail() != null ? user.getEmail() : "";
        String userGrade = user.getGrade() != null ? user.getGrade().getGrade() : "";
        Map<String, String> userInterests = user.getInterests() != null ? user.getInterests() : Map.of();

        return new MemberSignupResponse(user.getUserId(), nickname, email, userGrade, userInterests);
    }
}
