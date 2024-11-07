package com.sharafutdinov.aircraft_maintenance.service.airplane;


import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTO;

public interface AirplaneService {
    AirplaneDTO getAirplaneById(Long id);
    AirplaneDTO getAirplaneBySerialNumber(String serialNumber);
}
