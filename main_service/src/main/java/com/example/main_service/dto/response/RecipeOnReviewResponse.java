package com.example.main_service.dto.response;


import com.example.main_service.model.Dish;
import com.example.main_service.model.NationalCuisine;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class RecipeOnReviewResponse {

    private Long recipeNumber;
    private String description;
    private Integer countPortion;
    private String userLogin;
    private NationalCuisine nationalCuisine;
    private Dish dish;


    public RecipeOnReviewResponse(Long recipeNumber, String description, Integer countPortion,
                          String userLogin, NationalCuisine nationalCuisine, Dish dish) {
        this.recipeNumber = recipeNumber;
        this.description = description;
        this.countPortion = countPortion;
        this.userLogin = userLogin;
        this.nationalCuisine = nationalCuisine;
        this.dish = dish;
    }

}
