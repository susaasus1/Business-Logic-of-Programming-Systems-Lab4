package com.example.main_service.camunda.recipe;

import com.example.main_service.dto.EmailDetails;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.RecipeOnReviewService;
import com.example.main_service.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class DeclineRecipe implements JavaDelegate {
    private final RecipeOnReviewService recipeOnReviewService;

    private final UserService userService;


    public DeclineRecipe(RecipeOnReviewService recipeOnReviewService, UserService userService) {
        this.recipeOnReviewService = recipeOnReviewService;
        this.userService = userService;
    }


    @Override
    public void execute(DelegateExecution delegateExecution) {
        String role = (String) delegateExecution.getVariable("role");
        if (!role.equals("ROLE_ADMIN")) {
            delegateExecution.setVariable("errorMessage", "Недостаточно прав для выполнения этой операции!");
            throw new BpmnError("recipeError");
        }

        Long recipeId = Long.valueOf((Integer) delegateExecution.getVariable("recipeId"));
        String declineReason = (String) delegateExecution.getVariable("declineReason");

        String username = (String) delegateExecution.getVariable("username");
        try {
            recipeOnReviewService.deleteRecipe(recipeId, username, declineReason);
            EmailDetails emailDetails = new EmailDetails(userService.findUserByLogin(username).getEmail(),
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
