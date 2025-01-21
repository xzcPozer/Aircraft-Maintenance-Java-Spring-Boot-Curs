package com.sharafutdinov.aircraft_maintenance.request;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateScheduledCheckRequest {
    @NotNull(message = "выберите дату проверки")
    private LocalDateTime date;

    @NotNull(message = "выберите статус проверки")
    private CheckStatus status;
}
