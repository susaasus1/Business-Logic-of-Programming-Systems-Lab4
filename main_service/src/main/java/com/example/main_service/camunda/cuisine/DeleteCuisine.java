package com.example.main_service.camunda.cuisine;

import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.NationalCuisineService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;



@Component
public class DeleteCuisine implements JavaDelegate {

    private final NationalCuisineService nationalCuisineService;


    public DeleteCuisine(NationalCuisineService nationalCuisineService) {
        this.nationalCuisineService = nationalCuisineService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String role = (String) delegateExecution.getVariable("role");
        if (!role.equals("ROLE_ADMIN")) {
            delegateExecution.setVariable("errorMessage", "Недостаточно прав для выполнения этой операции!");
            throw new BpmnError("cuisineError");
        }
        Integer cuisineId = (Integer) delegateExecution.getVariable("cuisineId");
        try {
            nationalCuisineService.deleteCuisine(Long.valueOf(cuisineId));
        } catch (ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("cuisineError");
        }


    }
}
