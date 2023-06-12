package com.example.main_service.camunda.taste;

import com.example.main_service.exception.IllegalPageParametersException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.TastesService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component

public class GetAllTastes implements JavaDelegate {
    private final TastesService tastesService;

    public GetAllTastes(TastesService tastesService) {
        this.tastesService = tastesService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Integer page = Integer.parseInt((String) delegateExecution.getVariable("page"));
        Integer size = Integer.parseInt((String) delegateExecution.getVariable("size"));

        if (page == null) page = 1;
        if (size == null) size = 10;

        try {
            delegateExecution.setVariable("allTastes",
                    Arrays.toString(tastesService.getAllTaste(page, size).getContent().toArray()));
        } catch (IllegalPageParametersException | ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("tasteError");
        }
    }
}
