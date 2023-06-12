package com.example.main_service.camunda.ingredient;

import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.IngredientsService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component

public class DeleteIngredient implements JavaDelegate {
    private final IngredientsService ingredientsService;

    public DeleteIngredient(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.parseLong((String) delegateExecution.getVariable("id"));
        try {
            ingredientsService.deleteIngredient(id);
            delegateExecution.setVariable("ingredientId", id);
        } catch (ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("ingredientError");
        }
    }
}
