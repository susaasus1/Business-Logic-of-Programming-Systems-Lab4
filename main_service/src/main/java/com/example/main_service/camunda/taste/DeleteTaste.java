package com.example.main_service.camunda.taste;

import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.service.TastesService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DeleteTaste implements JavaDelegate {
    private final TastesService tastesService;

    public DeleteTaste(TastesService tastesService) {
        this.tastesService = tastesService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.parseLong((String) delegateExecution.getVariable("id"));
        try {
            tastesService.deleteTaste(id);
            delegateExecution.setVariable("tasteId", id);
        } catch (ResourceNotFoundException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("tasteError");
        }
    }
}
