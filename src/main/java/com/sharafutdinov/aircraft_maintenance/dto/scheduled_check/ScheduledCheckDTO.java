package com.sharafutdinov.aircraft_maintenance.dto.scheduled_check;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.enums.CheckType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledCheckDTO {
    private CheckType type;
    private LocalDateTime date;
    private CheckStatus status;
    private String serialNumber;
}
