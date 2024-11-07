package com.sharafutdinov.aircraft_maintenance.dto.performed_work;

import com.sharafutdinov.aircraft_maintenance.enums.WorkResult;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PerformedWorkDTO {
    private String workName;
    private String description;
    private LocalDateTime completionDate;
    private WorkResult result;
    private String serialNumber;
}
