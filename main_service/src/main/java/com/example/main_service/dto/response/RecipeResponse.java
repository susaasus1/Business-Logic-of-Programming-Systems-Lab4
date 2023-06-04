package com.example.main_service.dto.response;


import com.example.main_service.model.Dish;
import com.example.main_service.model.Ingredients;
import com.example.main_service.model.NationalCuisine;
import com.example.main_service.model.Tastes;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class RecipeResponse {

    private Long recipeNumber;
    private String description;
    private Integer countPortion;
    private String userLogin;
    private NationalCuisine nationalCuisine;
    private Dish dish;
    private List<Tastes> tastes;
    private List<Ingredients> ingredients;

    public RecipeResponse(Long recipeNumber, String description, Integer countPortion,
                          String userLogin, NationalCuisine nationalCuisine, Dish dish, List<Tastes> tastes, List<Ingredients> ingredients) {
        this.recipeNumber = recipeNumber;
        this.description = description;
        this.countPortion = countPortion;
        this.userLogin = userLogin;
        this.nationalCuisine = nationalCuisine;
        this.dish = dish;
        this.tastes = tastes;
        this.ingredients = ingredients;
    }

}
