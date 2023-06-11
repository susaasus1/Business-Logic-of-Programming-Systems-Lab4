package com.example.main_service.camunda.recipe;

import com.example.main_service.dto.request.AddRecipeRequest;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.RecipeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddRecipe implements JavaDelegate {

    private final RecipeService recipeService;

    public AddRecipe(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String username = (String) delegateExecution.getVariable("username");

        String dishName = (String) delegateExecution.getVariable("dishName");
        String description = (String) delegateExecution.getVariable("description");
        Integer countPortion = (Integer) delegateExecution.getVariable("countPortion");
        String nationalCuisineName = (String) delegateExecution.getVariable("nationalCuisineName");
        List<String> tastesNames = Arrays.stream(((String) delegateExecution.getVariable("tastesNames")).split(",")).map(String::trim).toList();
        List<String> ingredientsNames = Arrays.stream(((String) delegateExecution.getVariable("ingredientsNames")).split(",")).map(String::trim).toList();


        if (dishName.isBlank() || dishName.isEmpty() || dishName.length() > 255) {
            delegateExecution.setVariable("errorMessage", "Введено некорректное название блюда! От 1 до 255 символов");
            throw new BpmnError("recipeError");
        }

        if (description.isBlank() || description.isEmpty() || description.length() > 4096) {
            delegateExecution.setVariable("errorMessage", "Введено некорректное описание! От 1 до 4096 символов");
            throw new BpmnError("recipeError");
        }

        if (countPortion == null || countPortion < 1 || countPortion > 100) {
            delegateExecution.setVariable("errorMessage", "Введено некорректное количество! От 1 до 100!");
            throw new BpmnError("recipeError");
        }

        if (nationalCuisineName.isBlank() || nationalCuisineName.isEmpty() || nationalCuisineName.length() > 64) {
            delegateExecution.setVariable("errorMessage", "Введено некорректное название кухни! От 1 до 64 символов");
            throw new BpmnError("recipeError");
        }

        if (tastesNames.isEmpty()) {
            delegateExecution.setVariable("errorMessage", "Укажите вкусы!");
            throw new BpmnError("recipeError");
        }

        if (ingredientsNames.isEmpty()) {
            delegateExecution.setVariable("errorMessage", "Укажите ингредиенты!");
            throw new BpmnError("recipeError");
        }


        AddRecipeRequest addRecipeRequest = new AddRecipeRequest(
                dishName, description, countPortion, nationalCuisineName,
                tastesNames, ingredientsNames
        );

        try {
            recipeService.saveRecipe(username, addRecipeRequest);
        } catch (ResourceNotFoundException | UsernameNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("recipeError");
        }

    }
}
