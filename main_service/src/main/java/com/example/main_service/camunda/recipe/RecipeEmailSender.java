package com.example.main_service.camunda.recipe;

import com.example.main_service.dto.EmailDetails;
import com.example.main_service.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class RecipeEmailSender implements JavaDelegate {

    private final EmailService emailService;

    public RecipeEmailSender(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws JsonProcessingException {
        String emailDetailsJson = (String) delegateExecution.getVariable("emailDetailsObj");
        ObjectMapper objectMapper = new ObjectMapper();
        EmailDetails emailDetails = objectMapper.readValue(emailDetailsJson, EmailDetails.class);

        emailService.sendSimpleMail(emailDetails);


    }
}
