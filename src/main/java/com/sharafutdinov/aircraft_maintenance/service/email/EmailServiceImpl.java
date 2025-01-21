package com.sharafutdinov.aircraft_maintenance.service.email;

import com.sharafutdinov.aircraft_maintenance.enums.EmailTemplates;
import com.sharafutdinov.aircraft_maintenance.request.SendEmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    @Async
    public void sendScheduledTaskEmail(SendEmailRequest request, boolean isUpdated) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper =
                new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom(request.getFrom());
        String templateName;
        if(isUpdated){
            templateName = EmailTemplates.UPDATED_SCHEDULED_TASK.getTemplate();
            messageHelper.setSubject(EmailTemplates.UPDATED_SCHEDULED_TASK.getSubject());
        } else {
            templateName = EmailTemplates.SCHEDULED_TASK.getTemplate();
            messageHelper.setSubject(EmailTemplates.SCHEDULED_TASK.getSubject());
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("serialNumber",request.getScheduledCheckDTO().getSerialNumber());
        variables.put("type",request.getScheduledCheckDTO().getType());
        variables.put("date",request.getScheduledCheckDTO().getDate());
        variables.put("status",request.getScheduledCheckDTO().getStatus());

        Context context = new Context();
        context.setVariables(variables);

        try{
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(request.getTo());
            mailSender.send(mimeMessage);

            log.info(String.format("Email successfully sent to %s with template %s,", request.getTo(), templateName));
        }catch (MessagingException e){
            log.warn("Cannot send email to {}", request.getTo());
        }
    }
}
