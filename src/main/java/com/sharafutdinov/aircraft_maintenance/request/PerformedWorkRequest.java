package com.sharafutdinov.aircraft_maintenance.request;

import com.sharafutdinov.aircraft_maintenance.enums.WorkResult;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Data
public class PerformedWorkRequest {
    @NotEmpty(message = "заполните название")
    @NotNull(message = "заполните название")
    private String workName;

    @Size(max = 140, message = "поле должно быть не больше 140 символов")
    @Nullable
    private String description;

    @Nullable
    private LocalDateTime completionDate;

    @NotNull(message = "выберите результат")
    private WorkResult result;

    @NotEmpty(message = "выберите серийный номер самолета")
    @NotNull(message = "выберите серийный номер самолета")
    private String airplaneSerialNumber;

    public PerformedWorkRequest() {
        this.completionDate = LocalDateTime.now();
    }
}
