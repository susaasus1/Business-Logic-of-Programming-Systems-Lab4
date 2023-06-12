package com.example.main_service.camunda.taste;

import com.example.main_service.dto.request.AddTasteRequest;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.model.Tastes;
import com.example.main_service.repository.TastesRepository;
import com.example.main_service.service.TastesService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class AddTaste implements JavaDelegate {
    private final TastesService tastesService;

    public AddTaste(TastesService tastesService) {
        this.tastesService = tastesService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String taste = (String) delegateExecution.getVariable("taste");
        try {
            Tastes tastes = tastesService.saveTaste(new AddTasteRequest(taste));
            delegateExecution.setVariable("tasteId", tastes.getId());
        } catch (ResourceAlreadyExistException e) {
            delegateExecution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("tasteError");
        }
    }
}
