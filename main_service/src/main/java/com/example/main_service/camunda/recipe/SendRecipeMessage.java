package com.example.main_service.camunda.recipe;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SendRecipeMessage implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("emailSend")
                .setVariable("emailDetailsObj", delegateExecution.getVariable("emailDetailsObj"))
                .correlate();
    }
}
