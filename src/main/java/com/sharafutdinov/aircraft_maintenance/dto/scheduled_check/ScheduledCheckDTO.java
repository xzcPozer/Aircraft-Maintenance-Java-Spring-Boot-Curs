package com.sharafutdinov.aircraft_maintenance.dto.scheduled_check;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.enums.CheckType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledCheckDTO {
    private Long id;
    private CheckType type;
    private LocalDateTime date;
    private CheckStatus status;
    private String serialNumber;
}
