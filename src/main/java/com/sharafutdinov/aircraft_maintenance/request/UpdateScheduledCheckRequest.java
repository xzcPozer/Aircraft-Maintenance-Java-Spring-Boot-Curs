package com.sharafutdinov.aircraft_maintenance.request;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateScheduledCheckRequest {
    private LocalDateTime date;
    private CheckStatus status;
}
