package com.example.main_service.camunda.dish;

import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.model.Dish;
import com.example.main_service.model.Ingredients;
import com.example.main_service.service.DishService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component

public class GetDish implements JavaDelegate {
    private final DishService dishService;

    public GetDish(DishService dishService) {
        this.dishService = dishService;
    }
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.parseLong((String) delegateExecution.getVariable("id"));
        try {
            Dish dish = dishService.getDish(id);
            delegateExecution.setVariable("dish", dish.toString());
        } catch (ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("dishError");
        }
    }
}
