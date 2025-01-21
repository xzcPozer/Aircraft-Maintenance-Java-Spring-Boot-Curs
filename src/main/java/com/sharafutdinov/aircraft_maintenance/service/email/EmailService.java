package com.sharafutdinov.aircraft_maintenance.service.email;

import com.sharafutdinov.aircraft_maintenance.request.SendEmailRequest;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendScheduledTaskEmail(SendEmailRequest request, boolean isUpdated) throws MessagingException;
}
