package com.example.main_service.camunda.recipe;

import com.example.main_service.mapper.RecipeDTOMapper;
import com.example.main_service.service.RecipeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class GetRecipeOfTheDay implements JavaDelegate {
    private final RecipeService recipeService;

    private final RecipeDTOMapper recipeDTOMapper;

    public GetRecipeOfTheDay(RecipeService recipeService, RecipeDTOMapper recipeDTOMapper) {
        this.recipeService = recipeService;
        this.recipeDTOMapper = recipeDTOMapper;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            delegateExecution.setVariable("recipeOfTheDay", recipeDTOMapper.apply(
                    recipeService.getRecipeOfTheDay()).toString());
        } catch (Exception e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("recipeError");
        }
    }
}
