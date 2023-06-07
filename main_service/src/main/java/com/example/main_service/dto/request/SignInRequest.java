package com.example.main_service.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SignInRequest {
    @NotBlank(message = "Укажите логин!")
    @Size(min = 5, max = 255, message = "Укажите логин! От 5 до 255 символов")
    private String login;
    @NotBlank(message = "Укажите пароль!")
    @Size(min = 8, max = 255, message = "Укажите пароль! От 8 до 255 символов")
    private String password;

    public SignInRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
