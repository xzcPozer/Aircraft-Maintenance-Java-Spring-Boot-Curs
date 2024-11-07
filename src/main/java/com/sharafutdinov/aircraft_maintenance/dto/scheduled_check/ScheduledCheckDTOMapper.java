package com.sharafutdinov.aircraft_maintenance.dto.scheduled_check;

import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ScheduledCheckDTOMapper implements Function<ScheduledCheck, ScheduledCheckDTO> {
    @Override
    public ScheduledCheckDTO apply(ScheduledCheck scheduledCheck) {
        ScheduledCheckDTO dto = new ScheduledCheckDTO();
        dto.setType(scheduledCheck.getType());
        dto.setDate(scheduledCheck.getDate());
        dto.setStatus(scheduledCheck.getStatus());
        dto.setSerialNumber(scheduledCheck.getAirplane().getSerialNumber());
        return dto;
    }
}
