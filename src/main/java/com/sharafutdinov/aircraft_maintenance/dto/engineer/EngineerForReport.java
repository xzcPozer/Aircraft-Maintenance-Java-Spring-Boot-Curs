package com.sharafutdinov.aircraft_maintenance.dto.engineer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class EngineerForReport{
    private String lastname;
    private String name;
    private String position;
    private String period;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public EngineerForReport(EngineerDTO engineerDTO, LocalDate date1) {
        this.lastname = engineerDTO.getLastName();
        this.name = engineerDTO.getFirstName();
        this.position = engineerDTO.getPosition();
        this.period = date1.format(formatter);
    }

    public EngineerForReport(EngineerDTO engineerDTO, LocalDate date1, LocalDate date2) {
        this.lastname = engineerDTO.getLastName();
        this.name = engineerDTO.getFirstName();
        this.position = engineerDTO.getPosition();
        this.period = date1.format(formatter) +" - "+date2.format(formatter);
    }

}
