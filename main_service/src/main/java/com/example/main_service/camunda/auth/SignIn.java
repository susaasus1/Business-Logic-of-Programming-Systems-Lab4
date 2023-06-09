package com.example.main_service.camunda.auth;

import com.example.main_service.dto.request.SignInRequest;
import com.example.main_service.dto.response.AccessAndRefreshToken;
import com.example.main_service.security.JwtUtils;
import com.example.main_service.service.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SignIn implements JavaDelegate {


    private final JwtUtils jwtUtils;
    private final UserService userService;

    public SignIn(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");

        if (username.length() < 5 || username.length() > 255) {
            delegateExecution.setVariable("errorMessage", "Некорректный логин! От 5 до 255 символов");
            throw new BpmnError("signInError");
        }

        if (password.length() < 8 || password.length() > 255) {
            delegateExecution.setVariable("errorMessage", "Некорректный пароль! От 8 до 255 символов");
            throw new BpmnError("signInError");
        }

        AccessAndRefreshToken accessAndRefreshToken;
        try {
            accessAndRefreshToken = userService.authUser(new SignInRequest(username, password));
        } catch (AuthenticationException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("signInError");
        }

        delegateExecution.setVariable("accessToken", accessAndRefreshToken.getAccessToken());
        delegateExecution.setVariable("refreshToken", accessAndRefreshToken.getRefreshToken());

        List<GrantedAuthority> authorities = (List<GrantedAuthority>) jwtUtils.getAuthoritiesFromToken(accessAndRefreshToken.getAccessToken());
        delegateExecution.setVariable("role", authorities.get(0).getAuthority());
        delegateExecution.removeVariable("password");

    }
}