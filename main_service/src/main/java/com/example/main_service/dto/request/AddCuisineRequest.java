package com.example.main_service.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class AddCuisineRequest {
    @NotBlank(message = "Укажите название кухни!")
    @Size(min = 1, max = 64, message = "Укажите название кухни! От 1 до 64 символов")
    private String cuisine;

    public AddCuisineRequest(String cuisine) {
        this.cuisine = cuisine;
    }
}
