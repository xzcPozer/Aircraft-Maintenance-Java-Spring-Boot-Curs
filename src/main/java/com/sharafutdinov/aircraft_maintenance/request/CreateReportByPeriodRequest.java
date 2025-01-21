package com.sharafutdinov.aircraft_maintenance.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
public class CreateReportByPeriodRequest {
    @NotEmpty(message = "выберите формат отчета")
    @NotNull(message = "выберите формат отчета")
    private String reportFormat;

    @NotNull(message = "введите период")
    private LocalDate date1;

    @Nullable
    private LocalDate date2;
}
