package com.example.main_service.camunda.ingredient;

import com.example.main_service.exception.IllegalPageParametersException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.IngredientsService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component

public class GetAllIngredient implements JavaDelegate {
    private final IngredientsService ingredientsService;

    public GetAllIngredient(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Integer page = Integer.parseInt((String) delegateExecution.getVariable("page"));
        Integer size = Integer.parseInt((String) delegateExecution.getVariable("size"));

        if (page == null) page = 1;
        if (size == null) size = 10;

        try {
            delegateExecution.setVariable("allIngredients",
                    Arrays.toString(ingredientsService.getIngredientsPage(page, size).getContent().toArray()));
        } catch (IllegalPageParametersException | ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("ingredientsError");
        }
    }
}
