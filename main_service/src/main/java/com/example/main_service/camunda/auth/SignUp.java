package com.example.main_service.camunda.auth;

import com.example.main_service.dto.request.SignUpRequest;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.model.ERole;
import com.example.main_service.service.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SignUp implements JavaDelegate {

    private final UserService userService;

    public SignUp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        String email = (String) delegateExecution.getVariable("email");


        if (username.length() < 5 || username.length() > 255) {
            delegateExecution.setVariable("errorMessage", "Некорректный логин! От 5 до 255 символов");
            throw new BpmnError("signUpError");
        }

        if (password.length() < 8 || password.length() > 255) {
            delegateExecution.setVariable("errorMessage", "Некорректный пароль! От 8 до 255 символов");
            throw new BpmnError("signUpError");
        }

        if (email.length() < 1 || email.length() > 255) {
            delegateExecution.setVariable("errorMessage", "Некорректная электронная почта! От 1 до 255 символов");
            throw new BpmnError("signUpError");
        }

        try {
            userService.saveNewUser(new SignUpRequest(username, password, email), ERole.ROLE_USER);
        } catch (ResourceAlreadyExistException | ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("signUpError");
        }

    }
}
