package com.example.main_service.camunda.recipe;

import com.example.main_service.exception.IllegalPageParametersException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.mapper.RecipeOnReviewDTOMapper;
import com.example.main_service.service.RecipeOnReviewService;
import com.example.main_service.service.RecipeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class GetAllRecipesOnReview implements JavaDelegate {

    private final RecipeOnReviewService recipeOnReviewService;

    private final RecipeOnReviewDTOMapper recipeOnReviewDTOMapper;

    public GetAllRecipesOnReview(RecipeOnReviewService recipeOnReviewService, RecipeOnReviewDTOMapper recipeOnReviewDTOMapper) {
        this.recipeOnReviewService = recipeOnReviewService;
        this.recipeOnReviewDTOMapper = recipeOnReviewDTOMapper;
    }


    @Override
    public void execute(DelegateExecution delegateExecution) {
        Integer page = (Integer) delegateExecution.getVariable("page");
        Integer size = (Integer) delegateExecution.getVariable("size");

        if (page == null) page = 0;
        if (size == null) size = 10;

        try {
            delegateExecution.setVariable("allRecipesOnReview",
                    recipeOnReviewService.getAllRecipesOnReview(page, size, "DESC").stream()
                            .map(recipeOnReviewDTOMapper)
                            .toList().toString());
        } catch (IllegalPageParametersException | ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("recipeError");
        }

    }
}
