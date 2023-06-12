package com.example.main_service.camunda.dish;

import com.example.main_service.exception.IllegalPageParametersException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.DishService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component

public class GetAllDish implements JavaDelegate {
    private final DishService dishService;

    public GetAllDish(DishService dishService) {
        this.dishService = dishService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Integer page = Integer.parseInt((String) delegateExecution.getVariable("page"));
        Integer size = Integer.parseInt((String) delegateExecution.getVariable("size"));

        if (page == null) page = 1;
        if (size == null) size = 10;

        try {
            delegateExecution.setVariable("allDishes",
                    Arrays.toString(dishService.getAllDish(page, size).getContent().toArray()));
        } catch (IllegalPageParametersException | ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("dishError");
        }
    }
}
