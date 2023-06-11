package com.example.main_service.camunda.recipe;

import com.example.main_service.service.RecipeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class RecipeScheduled implements JavaDelegate {
    private final RecipeService recipeService;

    public RecipeScheduled(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        recipeService.updateRecipeOfTheDay();
    }
}
