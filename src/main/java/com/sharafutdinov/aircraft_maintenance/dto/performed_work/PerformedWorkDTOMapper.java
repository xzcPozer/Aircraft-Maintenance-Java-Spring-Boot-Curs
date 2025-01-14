package com.sharafutdinov.aircraft_maintenance.dto.performed_work;

import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

@Service
public class PerformedWorkDTOMapper implements Function<PerformedWork, PerformedWorkDTO> {

    @Override
    public PerformedWorkDTO apply(PerformedWork performedWork) {
        PerformedWorkDTO dto = new PerformedWorkDTO();
        dto.setWorkName(performedWork.getWorkName());
        dto.setDescription(performedWork.getDescription());
        dto.setCompletionDate(Date.from(performedWork.getCompletionDate().atZone(ZoneId.systemDefault()).toInstant()));
        dto.setResult(performedWork.getResult().name());
        dto.setSerialNumber(performedWork.getAirplane().getSerialNumber());
        return dto;
    }
}
