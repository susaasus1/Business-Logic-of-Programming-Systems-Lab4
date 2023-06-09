package com.example.main_service.camunda.taste;

import com.example.main_service.model.Tastes;
import com.example.main_service.repository.TastesRepository;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class AddTaste implements JavaDelegate {
    private final TastesRepository tastesRepository;

    public AddTaste(TastesRepository tastesRepository) {
        this.tastesRepository = tastesRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String taste = (String) delegateExecution.getVariable("taste");
        if (tastesRepository.existsTastesByTaste(taste))
            throw new BpmnError("Taste already exist");
        Tastes tastes = new Tastes(taste);
        tastesRepository.save(tastes);
        delegateExecution.setVariable("tasteId", tastes.getId());
    }
}
