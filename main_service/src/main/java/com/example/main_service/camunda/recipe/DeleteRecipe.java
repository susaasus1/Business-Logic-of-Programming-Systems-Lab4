package com.example.main_service.camunda.recipe;

import com.example.main_service.exception.PermissionDeniedException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.RecipeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DeleteRecipe implements JavaDelegate {
    private final RecipeService recipeService;

    public DeleteRecipe(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String username = (String) delegateExecution.getVariable("username");
        Long recipeId = Long.valueOf((Integer) delegateExecution.getVariable("recipeId"));


        try {
            recipeService.deleteRecipe(username,recipeId);
        }catch (ResourceNotFoundException | UsernameNotFoundException | PermissionDeniedException e){
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("recipeError");
        }

    }
}
