package com.example.main_service.camunda.recipe;

import com.example.main_service.exception.IllegalPageParametersException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.mapper.RecipeDTOMapper;
import com.example.main_service.service.RecipeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class GetAllRecipes implements JavaDelegate {


    private final RecipeService recipeService;

    private final RecipeDTOMapper recipeDTOMapper;

    public GetAllRecipes(RecipeService recipeService, RecipeDTOMapper recipeDTOMapper) {
        this.recipeService = recipeService;
        this.recipeDTOMapper = recipeDTOMapper;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Integer page = (Integer) delegateExecution.getVariable("page");
        Integer size = (Integer) delegateExecution.getVariable("size");

        if (page == null) page = 0;
        if (size == null) size = 10;

        try {
            delegateExecution.setVariable("allRecipes",
                    recipeService.getAllRecipes(page, size, "DESC").stream()
                            .map(recipeDTOMapper)
                            .toList().toString());
        } catch (IllegalPageParametersException | ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("recipeError");
        }

    }
}
