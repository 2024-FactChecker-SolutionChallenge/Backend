package com.solutionchallenge.factchecker.api.Member.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class LoginRequestDto {

    String id;
    String password;
}
