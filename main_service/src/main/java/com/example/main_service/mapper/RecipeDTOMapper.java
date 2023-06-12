package com.example.main_service.mapper;


import com.example.main_service.dto.response.RecipeResponse;
import com.example.main_service.model.*;
import com.example.main_service.repository.IngredientsRepository;
import com.example.main_service.repository.RecipeIngredientsRepository;
import com.example.main_service.repository.RecipeTastesRepository;
import com.example.main_service.repository.TastesRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class RecipeDTOMapper implements Function<Recipe, RecipeResponse> {

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    private final RecipeTastesRepository recipeTastesRepository;

    private final IngredientsRepository ingredientsRepository;

    private final TastesRepository tastesRepository;


    public RecipeDTOMapper(RecipeIngredientsRepository recipeIngredientsRepository, RecipeTastesRepository recipeTastesRepository, IngredientsRepository ingredientsRepository, TastesRepository tastesRepository) {
        this.recipeIngredientsRepository = recipeIngredientsRepository;
        this.recipeTastesRepository = recipeTastesRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.tastesRepository = tastesRepository;
    }

    @Override
    public RecipeResponse apply(Recipe recipe) {

        List<Ingredients> ingredients = recipeIngredientsRepository.getAllByRecipeId(recipe.getId()).stream().map(x -> ingredientsRepository.findIngredientsById(x.getIngredientId()).get())
                .toList();
        List<Tastes> tastes = recipeTastesRepository.getAllByRecipeId(recipe.getId()).stream().map(x -> tastesRepository.findTastesById(x.getTasteId()).get())
                .toList();

        return new RecipeResponse(
                recipe.getId(),
                recipe.getDescription(),
                recipe.getCountPortion(),
                recipe.getUser().getLogin(),
                recipe.getNationalCuisine(),
                recipe.getDish(),
                tastes,
                ingredients
        );
    }
}
