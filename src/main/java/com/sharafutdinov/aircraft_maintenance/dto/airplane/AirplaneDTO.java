package com.sharafutdinov.aircraft_maintenance.dto.airplane;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AirplaneDTO {
    private String serialNumber;
    private String model;
    private LocalDate yearOfRelease;
}
