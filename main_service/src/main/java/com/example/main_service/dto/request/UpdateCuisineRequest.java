package com.example.main_service.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCuisineRequest {
    private String cuisine;

    public UpdateCuisineRequest(String cuisine) {
        this.cuisine = cuisine;
    }
}
