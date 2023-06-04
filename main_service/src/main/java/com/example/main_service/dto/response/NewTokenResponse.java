package com.example.main_service.dto.response;

import lombok.Data;

@Data
public class NewTokenResponse {
    private String token;
    private String refreshToken;

    public NewTokenResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
