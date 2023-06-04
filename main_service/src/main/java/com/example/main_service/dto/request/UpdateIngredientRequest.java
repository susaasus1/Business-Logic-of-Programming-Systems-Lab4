package com.example.main_service.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateIngredientRequest {
    private String description;
    private String name;

    public UpdateIngredientRequest(String description, String name) {
        this.description = description;
        this.name = name;
    }
}
