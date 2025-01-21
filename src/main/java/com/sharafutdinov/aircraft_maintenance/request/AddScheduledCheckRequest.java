package com.sharafutdinov.aircraft_maintenance.request;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.enums.CheckType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddScheduledCheckRequest {
    @NotNull(message = "выберите тип проверки")
    private CheckType type;

    @NotNull(message = "выберите дату проверки")
    private LocalDateTime date;

    @NotNull(message = "выберите статус проверки")
    private CheckStatus status;

    @NotEmpty(message = "выберите серийный номер самолета")
    @NotNull(message = "выберите серийный номер самолета")
    private String airplaneSerialNumber;
}
