package com.sharafutdinov.aircraft_maintenance.request;

import com.sharafutdinov.aircraft_maintenance.enums.WorkResult;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PerformedWorkRequest {
    private String workName;
    private String description;
    private LocalDateTime completionDate;
    private WorkResult result;
    private String airplaneSerialNumber;
}
