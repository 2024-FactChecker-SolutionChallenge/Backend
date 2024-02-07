package com.solutionchallenge.factchecker.global.dto.response;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalTokenResponse {
    private String msg;
    private int statusCode;

    public GlobalTokenResponse(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

}
