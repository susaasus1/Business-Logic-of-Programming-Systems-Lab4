package com.example.main_service.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SignUpRequest {
    @NotBlank(message = "Укажите логин!")
    @Size(min = 5, max = 255, message = "Укажите логин! От 5 до 255 символов")
    private String login;
    @NotBlank(message = "Укажите пароль!")
    @Size(min = 8, max = 255, message = "Укажите пароль! От 8 до 255 символов")
    private String password;

    @NotBlank(message = "Укажите электронную почту!")
    @Email(message = "Неверный формат электронной почты!")
    @Size(min = 1, max = 255, message = "Укажите электронную почту! От 1 до 255 символов")
    private String email;


    public SignUpRequest(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

}
