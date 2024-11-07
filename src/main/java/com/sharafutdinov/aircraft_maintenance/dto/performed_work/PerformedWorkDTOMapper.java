package com.sharafutdinov.aircraft_maintenance.dto.performed_work;

import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PerformedWorkDTOMapper implements Function<PerformedWork, PerformedWorkDTO> {

    @Override
    public PerformedWorkDTO apply(PerformedWork performedWork) {
        PerformedWorkDTO dto = new PerformedWorkDTO();
        dto.setWorkName(performedWork.getWorkName());
        dto.setDescription(performedWork.getDescription());
        dto.setCompletionDate(performedWork.getCompletionDate());
        dto.setResult(performedWork.getResult());
        dto.setSerialNumber(performedWork.getAirplane().getSerialNumber());
        return dto;
    }
}
