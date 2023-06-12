package com.example.main_service.camunda.ingredient;

import com.example.main_service.dto.request.AddIngredientRequest;
import com.example.main_service.dto.request.AddTasteRequest;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.model.Ingredients;
import com.example.main_service.model.Tastes;
import com.example.main_service.service.IngredientsService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component

public class AddIngredient implements JavaDelegate {
    private final IngredientsService ingredientsService;

    public AddIngredient(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String name = (String) delegateExecution.getVariable("name");
        String description = (String) delegateExecution.getVariable("description");
        try {
            Ingredients ingredients = ingredientsService.saveIngredient(new AddIngredientRequest(name, description));
            delegateExecution.setVariable("ingredientId", ingredients.getId());
        } catch (ResourceAlreadyExistException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("ingredientError");
        }
    }
}
