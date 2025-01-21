package com.sharafutdinov.aircraft_maintenance.service.notification;

import com.sharafutdinov.aircraft_maintenance.model.Notification;
import com.sharafutdinov.aircraft_maintenance.repository.NotificationRepository;
import com.sharafutdinov.aircraft_maintenance.request.SendEmailRequest;
import com.sharafutdinov.aircraft_maintenance.service.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository repository;
    private final EmailService emailService;
    @Override
    public void sendNotification(SendEmailRequest sendEmailConfirmation, boolean isUpdated) throws MessagingException {
        log.info(String.format("Send the message: %s", sendEmailConfirmation));

        Notification notification = new Notification();
        notification.setType(sendEmailConfirmation.getScheduledCheckDTO().getType());
        notification.setDate(sendEmailConfirmation.getScheduledCheckDTO().getDate());
        notification.setSerialNumber(sendEmailConfirmation.getScheduledCheckDTO().getSerialNumber());

        repository.save(notification);

        emailService.sendScheduledTaskEmail(sendEmailConfirmation, isUpdated);
    }
}
