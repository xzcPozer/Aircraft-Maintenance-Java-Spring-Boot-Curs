package com.sharafutdinov.aircraft_maintenance.service.notification;

import com.sharafutdinov.aircraft_maintenance.request.SendEmailRequest;
import jakarta.mail.MessagingException;

public interface NotificationService {

    void sendNotification(SendEmailRequest sendEmailConfirmation, boolean isUpdated) throws MessagingException;
}
