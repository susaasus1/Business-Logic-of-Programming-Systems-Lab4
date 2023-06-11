package com.example.main_service.camunda.recipe;

import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.mapper.RecipeDTOMapper;
import com.example.main_service.service.RecipeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
public class GetRecipe implements JavaDelegate {

    private final RecipeService recipeService;

    private final RecipeDTOMapper recipeDTOMapper;

    public GetRecipe(RecipeService recipeService, RecipeDTOMapper recipeDTOMapper) {
        this.recipeService = recipeService;
        this.recipeDTOMapper = recipeDTOMapper;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Long recipeId = Long.valueOf((Integer) delegateExecution.getVariable("recipeId"));
        try {
            delegateExecution.setVariable("recipe", recipeDTOMapper.apply(
                    recipeService.findRecipeById(recipeId)
            ).toString());
        } catch (ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("recipeError");
        }

    }
}
