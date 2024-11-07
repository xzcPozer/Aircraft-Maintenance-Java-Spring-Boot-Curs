package com.sharafutdinov.aircraft_maintenance.dto.engineer;

import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EngineerDTOMapper implements Function<Engineer, EngineerDTO> {
    @Override
    public EngineerDTO apply(Engineer engineer) {
        EngineerDTO dto = new EngineerDTO();
        dto.setFirstName(engineer.getFirstName());
        dto.setLastName(engineer.getLastName());
        dto.setPosition(engineer.getPosition());
        return dto;
    }
}
