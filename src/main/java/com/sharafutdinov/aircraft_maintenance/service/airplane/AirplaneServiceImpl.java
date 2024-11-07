package com.sharafutdinov.aircraft_maintenance.service.airplane;

import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTO;
import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTOMapper;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.repository.AirplaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {
    private final AirplaneRepository airplaneRepository;
    private final AirplaneDTOMapper airplaneDTOMapper;
    @Override
    public AirplaneDTO getAirplaneById(Long id) {
        return airplaneRepository.findById(id)
                .map(airplaneDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Самолет не был найден"));
    }

    @Override
    public AirplaneDTO getAirplaneBySerialNumber(String serialNumber) {
        return Optional.ofNullable(airplaneRepository.findBySerialNumber(serialNumber))
                .map(airplaneDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Самолет не был найден"));
    }
}
