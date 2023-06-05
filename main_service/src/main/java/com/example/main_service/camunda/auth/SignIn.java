package com.example.main_service.camunda.auth;

import com.example.main_service.security.CookUserDetails;
import com.example.main_service.security.CookUserDetailsService;
import com.example.main_service.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named
@Slf4j
public class SignIn implements JavaDelegate {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CookUserDetailsService cookUserDetailsService;

    public SignIn(AuthenticationManager authenticationManager, JwtUtils jwtUtils, CookUserDetailsService cookUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cookUserDetailsService = cookUserDetailsService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        CookUserDetails userDetails = (CookUserDetails) cookUserDetailsService.loadUserByUsername(username);

        String accessToken = jwtUtils.generateJWTToken(userDetails.getUsername(), userDetails.getAuthorities(), userDetails.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername(), userDetails.getAuthorities(), userDetails.getEmail());

        delegateExecution.setVariable("accessToken", accessToken);
        delegateExecution.setVariable("refreshToken", refreshToken);
        delegateExecution.setVariable("username", username);
    }
}
