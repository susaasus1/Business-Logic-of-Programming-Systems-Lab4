package com.example.main_service.mapper;


import com.example.main_service.dto.response.RecipeOnReviewResponse;
import com.example.main_service.model.RecipeOnReview;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class RecipeOnReviewDTOMapper implements Function<RecipeOnReview, RecipeOnReviewResponse>{


    @Override
    public RecipeOnReviewResponse apply(RecipeOnReview recipeOnReview) {
        return new RecipeOnReviewResponse(
                recipeOnReview.getId(),
                recipeOnReview.getDescription(),
                recipeOnReview.getCountPortion(),
                recipeOnReview.getUser().getLogin(),
                recipeOnReview.getNationalCuisine(),
                recipeOnReview.getDish()        );
    }
}
