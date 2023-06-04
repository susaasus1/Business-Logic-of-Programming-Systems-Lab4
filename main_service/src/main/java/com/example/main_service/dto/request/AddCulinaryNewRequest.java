package com.example.main_service.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class AddCulinaryNewRequest {
    @NotBlank(message = "Укажите название кулинарной новости!")
    @Size(min = 1, max = 255, message = "Укажите название кулинарной новости! От 1 до 255 символов")
    private String name;

    @NotBlank(message = "Укажите описание кулинарной новости!")
    @Size(min = 1, max = 4096, message = "Укажите описание кулинарной новости! От 1 до 4096 символов")
    private String description;

}
