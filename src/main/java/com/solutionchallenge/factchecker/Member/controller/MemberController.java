package com.solutionchallenge.factchecker.Member.controller;
import com.solutionchallenge.factchecker.Member.dto.request.MailConfirmRequest;
import com.solutionchallenge.factchecker.Member.dto.request.MemberLoginRequest;
import com.solutionchallenge.factchecker.Member.dto.request.MemberSignupRequest;
import com.solutionchallenge.factchecker.Member.dto.request.TokenInfo;
import com.solutionchallenge.factchecker.Member.dto.response.MemberSignupResponse;
import com.solutionchallenge.factchecker.Member.repository.MemberRepository;
import com.solutionchallenge.factchecker.Member.service.EmailService;
import com.solutionchallenge.factchecker.Member.service.MemberService;
import com.solutionchallenge.factchecker.global.auth.JwtTokenProvider;
import com.solutionchallenge.factchecker.global.dto.response.BaseResponse;
import com.solutionchallenge.factchecker.global.dto.response.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);


    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtUtil;
    private MemberRepository memberRepository;

    @Autowired
    public MemberController(MemberService userService, EmailService emailService, JwtTokenProvider jwtUtil, MemberRepository memberRepository) {
        this.memberService = userService;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }
    @Operation(summary = "이메일 인증", description = "이메일 인증번호 (ePw) 를 입력받은 메일로 전송합니다.")
    @PostMapping("/confirm")
    public ResponseEntity<?> emailConfirm(@RequestBody MailConfirmRequest dto) {
        try {
            // 이메일을 보내고 인증 코드를 생성
            String ePw = emailService.sendEmailAndGenerateCode(dto.getEmail());
            Map<String, String> confirmResult = new HashMap<>();
            confirmResult.put("AuthenticationCode", ePw);
            return ResponseEntity.ok(confirmResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("확인 이메일 전송 중 오류가 발생했습니다.");
        }
    }
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse<MemberSignupResponse>> registerUser(
            @RequestBody @Valid MemberSignupRequest usersignupRequest) {
        try {
            // 중복가입 방지
            if (memberRepository.existsMemberByEmail(usersignupRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new BaseResponse<>(ErrorCode.EMAIL_EXISTS_ERROR));
            }
            // 닉네임 중복검사
            if (memberRepository.existsMemberByNickname(usersignupRequest.getNickname())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new BaseResponse<>(ErrorCode.USERNAME_EXISTS_ERROR));
            }
            // 회원가입 진행
            MemberSignupResponse response = memberService.registerSignup(usersignupRequest);
            // 회원가입 성공 시 응답
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(response));
        } catch (Exception e) {
            // 예외가 발생한 경우 에러 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호를 사용하여 로그인합니다.")
    @PostMapping("/login")
    public TokenInfo login(
            @RequestBody @Valid MemberLoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        TokenInfo tokenInfo = memberService.login(email, password);
        return tokenInfo;
    }


//    @PostMapping("/login")
//    public ResponseEntity<BaseResponse<TokenResponse>> loginUser(
//            HttpServletResponse res,
//            @RequestBody MemberLoginRequest dto) throws Exception {
//
//        try {
//            logger.debug("Received login request: {}", dto); // 로그 추가
//            memberService.login(res, dto);
//            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("로그인에 성공했습니다."));
//        } catch (BadCredentialsException e) {
//            logger.error("Error occurred during login: {}", e.getMessage()); // 로그 추가
//            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>("인증 정보가 부족합니다."));
//        }
//    }

}