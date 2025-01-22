package com.sharafutdinov.aircraft_maintenance.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;
import java.awt.Desktop;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Nullable
    private String savePath;

    public CreateReportByPeriodRequest(){
        String userHome = System.getProperty("user.home");
        String desktopFolderName = "Desktop";
        Path desktopPath = Paths.get(userHome, desktopFolderName);
        this.savePath = desktopPath.toString();
    }
}
