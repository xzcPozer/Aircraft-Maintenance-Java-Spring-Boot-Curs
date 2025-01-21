package com.sharafutdinov.aircraft_maintenance.service.scheduled_task;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import jakarta.mail.MessagingException;

public interface ScheduledTaskService {
    void sendScheduledWorks() throws MessagingException;
    void sendUpdatedScheduledWorks(ScheduledCheck scheduledCheck) throws MessagingException;
}
