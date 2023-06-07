package com.example.main_service.camunda.cuisine;

import com.example.main_service.dto.request.AddCuisineRequest;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.model.NationalCuisine;
import com.example.main_service.service.NationalCuisineService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class AddCuisine implements JavaDelegate {

    private final NationalCuisineService nationalCuisineService;

    public AddCuisine(NationalCuisineService nationalCuisineService ) {
        this.nationalCuisineService = nationalCuisineService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String role = (String) delegateExecution.getVariable("role");
        if (!role.equals("ROLE_ADMIN")) {
            delegateExecution.setVariable("errorMessage", "Недостаточно прав для выполнения этой операции!");
            throw new BpmnError("cuisineError");
        }

        String cuisineName = (String) delegateExecution.getVariable("cuisineName");

        if (cuisineName.isBlank() || cuisineName.isEmpty() || cuisineName.length() > 64) {
            delegateExecution.setVariable("errorMessage", "Введено некорректное название кухни! От 1 до 64 символов");
            throw new BpmnError("cuisineError");
        }

        NationalCuisine nationalCuisine;
        try {
            nationalCuisine = nationalCuisineService.saveCuisine(new AddCuisineRequest(cuisineName));
        } catch (ResourceAlreadyExistException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("cuisineError");
        }

        delegateExecution.setVariable("cuisine", nationalCuisine.toString());

    }
}
