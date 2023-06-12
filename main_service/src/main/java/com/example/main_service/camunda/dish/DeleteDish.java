package com.example.main_service.camunda.dish;

import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.DishService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DeleteDish implements JavaDelegate {
    private final DishService dishService;

    public DeleteDish(DishService dishService) {
        this.dishService = dishService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.parseLong((String) delegateExecution.getVariable("id"));
        try {
            dishService.deleteDish(id);
            delegateExecution.setVariable("dishId", id);
        } catch (ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("dishError");
        }
    }
}
