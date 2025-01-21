package com.sharafutdinov.aircraft_maintenance.controller;

import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTO;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.response.ApiResponse;
import com.sharafutdinov.aircraft_maintenance.service.airplane.AirplaneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/airplanes")
@Tag(name = "Airplane")
public class AirplaneController {
    private final AirplaneService airplaneService;

    @GetMapping
    public ResponseEntity<List<AirplaneDTO>> getAllAirplanes() {
        List<AirplaneDTO> airplanes = airplaneService.findAllAirplanes();
        return ResponseEntity.ok(airplanes);
    }

    @GetMapping("/{airplaneId}")
    public ResponseEntity<AirplaneDTO> getAirplaneById(@PathVariable Long airplaneId) {
        AirplaneDTO airplane = airplaneService.getAirplaneById(airplaneId);
        return ResponseEntity.ok(airplane);
    }

    @GetMapping("/airplane/by/serial-number")
    public ResponseEntity<AirplaneDTO> getAirplaneBySerialNumber(@RequestParam String serialNum) {
        AirplaneDTO airplane = airplaneService.getAirplaneBySerialNumber(serialNum);
        return ResponseEntity.ok(airplane);
    }
}
