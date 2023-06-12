package com.example.main_service.camunda.taste;

import com.example.main_service.dto.request.AddTasteRequest;
import com.example.main_service.dto.request.UpdateTasteRequest;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.model.Tastes;
import com.example.main_service.service.TastesService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class UpdateTaste implements JavaDelegate {
    private final TastesService tastesService;

    public UpdateTaste(TastesService tastesService) {
        this.tastesService = tastesService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.parseLong((String) delegateExecution.getVariable("id"));
        String taste = (String) delegateExecution.getVariable("taste");
        try {
            Tastes tastes = tastesService.updateTaste(id, new UpdateTasteRequest(taste));
            delegateExecution.setVariable("tasteId", tastes.getId());
        } catch (ResourceAlreadyExistException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("tasteError");
        }
    }
}
