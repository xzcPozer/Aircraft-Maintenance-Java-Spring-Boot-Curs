package com.sharafutdinov.aircraft_maintenance.request;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.enums.CheckType;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AddScheduledCheckRequest {
    private CheckType type;
    private LocalDateTime date;
    private CheckStatus status;
    private String airplaneSerialNumber;
}
