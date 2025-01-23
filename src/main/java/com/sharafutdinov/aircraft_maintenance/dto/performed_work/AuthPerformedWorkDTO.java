package com.sharafutdinov.aircraft_maintenance.dto.performed_work;

import lombok.Data;

import java.util.Date;

@Data
public class AuthPerformedWorkDTO {
    private Long id;
    private String workName;
    private String description;
    private Date completionDate;
    private String result;
    private String serialNumber;
}
