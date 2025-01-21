package com.sharafutdinov.aircraft_maintenance.service.airplane;


import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTO;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;

import java.util.List;

public interface AirplaneService {

    AirplaneDTO getAirplaneById(Long id);
    AirplaneDTO getAirplaneBySerialNumber(String serialNumber);
    List<AirplaneDTO> findAllAirplanes();
}
