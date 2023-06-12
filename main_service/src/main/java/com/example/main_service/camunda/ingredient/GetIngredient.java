package com.example.main_service.camunda.ingredient;

import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.model.Ingredients;
import com.example.main_service.model.Tastes;
import com.example.main_service.service.IngredientsService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class GetIngredient implements JavaDelegate {
    private final IngredientsService ingredientsService;

    public GetIngredient(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.parseLong((String) delegateExecution.getVariable("id"));
        try {
            Ingredients ingredients = ingredientsService.getIngredient(id);
            delegateExecution.setVariable("ingredient", ingredients.toString());
        } catch (ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("ingredientError");
        }
    }
}
