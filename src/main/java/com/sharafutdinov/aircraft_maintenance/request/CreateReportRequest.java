package com.sharafutdinov.aircraft_maintenance.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateReportRequest {
    private String reportFormat;
    private LocalDate date1;
    private LocalDate date2;
}
