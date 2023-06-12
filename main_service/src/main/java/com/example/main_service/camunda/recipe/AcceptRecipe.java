package com.example.main_service.camunda.recipe;

import com.example.main_service.dto.EmailDetails;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.model.Recipe;
import com.example.main_service.service.RecipeOnReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class AcceptRecipe implements JavaDelegate {
    private final RecipeOnReviewService recipeOnReviewService;


    public AcceptRecipe(RecipeOnReviewService recipeOnReviewService) {
        this.recipeOnReviewService = recipeOnReviewService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution)   {
        String role = (String) delegateExecution.getVariable("role");
        if (!role.equals("ROLE_ADMIN")) {
            delegateExecution.setVariable("errorMessage", "Недостаточно прав для выполнения этой операции!");
            throw new BpmnError("recipeError");
        }

        Long recipeId = Long.valueOf((Integer) delegateExecution.getVariable("recipeId"));
        String username = (String) delegateExecution.getVariable("username");
        try {
            Recipe recipe = recipeOnReviewService.saveRecipe(recipeId);
            EmailDetails emailDetails = new EmailDetails(recipe.getUser().getEmail(),
                    "Ваш рецепт №" + recipeId + " был проверен и добавлен администратором " + username,
                    "Добавление рецепта на сайте povarenok.ru");
            ObjectMapper objectMapper = new ObjectMapper();
            String emailDetailsJson = objectMapper.writeValueAsString(emailDetails);

            delegateExecution.setVariable("emailDetailsObj", emailDetailsJson);
        } catch (ResourceNotFoundException | JsonProcessingException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("recipeError");
        }
    }
}
