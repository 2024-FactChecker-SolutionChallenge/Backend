package com.solutionchallenge.factchecker.Member.controller;
import com.solutionchallenge.factchecker.Member.dto.MailConfirmDto;
import com.solutionchallenge.factchecker.Member.dto.request.MemberSignupRequest;
import com.solutionchallenge.factchecker.Member.dto.response.MemberSignupResponse;
import com.solutionchallenge.factchecker.Member.service.EmailService;
import com.solutionchallenge.factchecker.Member.service.MemberService;
import com.solutionchallenge.factchecker.global.dto.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    private final MemberService userService;
    private final EmailService emailService;

    @Autowired
    public MemberController(MemberService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }
    @Operation(summary = "이메일 인증", description = "이메일 인증번호 (ePw) 를 입력받은 메일로 전송합니다.")
    @PostMapping("/confirm")
    public ResponseEntity<?> emailConfirm(@RequestBody MailConfirmDto dto) {
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

    @Operation(summary = "사용자 회원가입하기")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse<MemberSignupResponse>> registerUser(
            @RequestBody @Valid MemberSignupRequest usersignupRequestDTO) {
        MemberSignupResponse response = userService.registerSignup(usersignupRequestDTO);
        return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.CREATED);
    }

//    @Operation(summary = "사용자 로그인하기")
//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<BaseResponse<UserSigninResponse>> loginUser(
//            @RequestBody @Valid UserSigninRequest userSigninRequestDTO) {
//        UserSigninResponse response = userService.loginUser(userSigninRequestDTO);
//        return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.CREATED);
//    }

}