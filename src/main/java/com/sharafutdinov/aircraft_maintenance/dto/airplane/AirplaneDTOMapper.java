package com.sharafutdinov.aircraft_maintenance.dto.airplane;

import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AirplaneDTOMapper implements Function<Airplane, AirplaneDTO> {
    @Override
    public AirplaneDTO apply(Airplane airplane) {
        AirplaneDTO dto = new AirplaneDTO();
        dto.setId(airplane.getId());
        dto.setSerialNumber(airplane.getSerialNumber());
        dto.setModel(airplane.getModel());
        dto.setYearOfRelease(airplane.getYearOfRelease());
        return dto;
    }
}
