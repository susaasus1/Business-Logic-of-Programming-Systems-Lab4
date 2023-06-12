package com.example.main_service.camunda.dish;

import com.example.main_service.dto.request.AddDishRequest;
import com.example.main_service.dto.request.AddIngredientRequest;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.model.Dish;
import com.example.main_service.model.Ingredients;
import com.example.main_service.service.DishService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class AddDish implements JavaDelegate {
    private final DishService dishService;

    public AddDish(DishService dishService) {
        this.dishService = dishService;
    }
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String name = (String) delegateExecution.getVariable("name");
        String description = (String) delegateExecution.getVariable("description");
        try {
            Dish dish = dishService.saveDish(new AddDishRequest(name, description));
            delegateExecution.setVariable("dishId", dish.getId());
        } catch (ResourceAlreadyExistException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("dishError");
        }
    }
}
