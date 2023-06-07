package com.example.main_service.camunda.cuisine;

import com.example.main_service.dto.request.UpdateCuisineRequest;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.model.NationalCuisine;
import com.example.main_service.service.NationalCuisineService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class UpdateCuisine implements JavaDelegate {
    private final NationalCuisineService nationalCuisineService;


    public UpdateCuisine(NationalCuisineService nationalCuisineService) {
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
        String cuisineName = (String) delegateExecution.getVariable("cuisineName");

        if (cuisineId == null) {
            delegateExecution.setVariable("errorMessage", "Введите cuisineId!");
            throw new BpmnError("cuisineError");
        }

        if (cuisineName.isBlank() || cuisineName.isEmpty() || cuisineName.length() > 64) {
            delegateExecution.setVariable("errorMessage", "Введено некорректное название кухни! От 1 до 64 символов");
            throw new BpmnError("cuisineError");
        }

        NationalCuisine updatedNationalCuisine;
        try {
            updatedNationalCuisine = nationalCuisineService.updateCuisine(Long.valueOf(cuisineId), new UpdateCuisineRequest(cuisineName));
        } catch (ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("cuisineError");
        }


        delegateExecution.setVariable("updatedCuisine", updatedNationalCuisine.toString());

    }
}
