package com.sharafutdinov.aircraft_maintenance.service.airplane;


import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTO;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;

import java.util.List;

public interface AirplaneService {

    AirplaneDTO getAirplaneById(Long id);
    AirplaneDTO getAirplaneBySerialNumber(String serialNumber);
    PageResponse<AirplaneDTO> findAllAirplanes(int page, int size);
    List<String> findAllSerialNumbers();
}
