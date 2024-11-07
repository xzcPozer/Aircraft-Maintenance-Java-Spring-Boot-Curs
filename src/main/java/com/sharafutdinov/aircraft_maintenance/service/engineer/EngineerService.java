package com.sharafutdinov.aircraft_maintenance.service.engineer;

import com.sharafutdinov.aircraft_maintenance.dto.engineer.EngineerDTO;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;

public interface EngineerService {
    EngineerDTO getEngineerById(Long engineerId);
    Engineer getAuthenticatedEngineer();
}
