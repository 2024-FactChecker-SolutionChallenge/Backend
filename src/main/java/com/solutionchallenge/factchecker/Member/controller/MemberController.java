package com.solutionchallenge.factchecker.Member.controller;
import com.solutionchallenge.factchecker.Member.dto.request.MemberSignupRequest;
import com.solutionchallenge.factchecker.Member.dto.response.MemberSignupResponse;
import com.solutionchallenge.factchecker.Member.service.MemberService;
import com.solutionchallenge.factchecker.global.dto.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class MemberController {
    @Autowired
    private MemberService userService;

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