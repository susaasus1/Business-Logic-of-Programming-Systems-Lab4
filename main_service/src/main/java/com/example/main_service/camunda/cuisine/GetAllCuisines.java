package com.example.main_service.camunda.cuisine;

import com.example.main_service.exception.IllegalPageParametersException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.NationalCuisineService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GetAllCuisines implements JavaDelegate {

    private final NationalCuisineService nationalCuisineService;

    public GetAllCuisines(NationalCuisineService nationalCuisineService) {
        this.nationalCuisineService = nationalCuisineService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Integer page = (Integer) delegateExecution.getVariable("page");
        Integer size = (Integer) delegateExecution.getVariable("size");

        if (page == null) page = 1;
        if (size == null) size = 10;

        try {
            delegateExecution.setVariable("allCuisines",
                    Arrays.toString(nationalCuisineService.getCuisinesPage(page, size).getContent().toArray()));
        } catch (IllegalPageParametersException | ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("cuisineError");
        }

    }
}
