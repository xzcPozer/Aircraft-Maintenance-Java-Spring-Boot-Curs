package com.sharafutdinov.aircraft_maintenance.request;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendEmailRequest {
    ScheduledCheckDTO scheduledCheckDTO;
    String from;
    String to;
}
