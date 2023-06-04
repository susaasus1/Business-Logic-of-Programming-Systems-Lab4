package com.example.main_service.service;

import com.example.main_service.dto.request.RefreshTokenRequest;
import com.example.main_service.dto.response.NewTokenResponse;
import com.example.main_service.exception.TokenHasExpiredException;
import com.example.main_service.security.JwtUtils;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    private final JwtUtils jwtUtils;


    public RefreshTokenService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public NewTokenResponse createNewToken(RefreshTokenRequest refreshTokenRequest) {
        if (!jwtUtils.validateJwtToken(refreshTokenRequest.getRefreshToken())) {
            throw new TokenHasExpiredException(refreshTokenRequest.getRefreshToken(), "Токен истек");
        }
        String login = jwtUtils.getLoginFromJwtToken(refreshTokenRequest.getRefreshToken());
        String email = jwtUtils.getEmailFromToken(refreshTokenRequest.getRefreshToken());

        String accessToken = jwtUtils.generateJWTToken(login,
                jwtUtils.getAuthoritiesFromToken(refreshTokenRequest.getRefreshToken()), email);
        String refreshToken = jwtUtils.generateRefreshToken(
                login, jwtUtils.getAuthoritiesFromToken(refreshTokenRequest.getRefreshToken()), email);
        return new NewTokenResponse(accessToken, refreshToken);
    }
}
