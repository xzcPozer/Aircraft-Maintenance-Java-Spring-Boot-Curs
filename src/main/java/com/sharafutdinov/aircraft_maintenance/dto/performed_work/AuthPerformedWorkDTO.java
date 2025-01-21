package com.sharafutdinov.aircraft_maintenance.dto.performed_work;

import com.sharafutdinov.aircraft_maintenance.enums.WorkResult;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PerformedWorkDTO {
    private String workName;
    private String description;
    private Date completionDate;
    private String result;
    private String serialNumber;
}
