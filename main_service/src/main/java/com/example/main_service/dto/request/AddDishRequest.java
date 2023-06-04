package com.example.main_service.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddDishRequest {
    @NotBlank(message = "Укажите название блюда!")
    @Size(min = 1, max = 255, message = "Укажите название блюда! От 1 до 255 символов")
    private String dishName;
    @NotBlank(message = "Укажите описание рецепта!")
    @Size(min = 1, max = 512, message = "Укажите описание рецепта! От 1 до 512 символов")
    private String description;

    public AddDishRequest(String dishName, String description) {
        this.dishName = dishName;
        this.description = description;
    }
}
