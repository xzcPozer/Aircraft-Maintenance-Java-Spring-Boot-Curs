package com.sharafutdinov.aircraft_maintenance.controller;

import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTO;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.response.ApiResponse;
import com.sharafutdinov.aircraft_maintenance.service.airplane.AirplaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/airplanes")
public class AirplaneController {
    private final AirplaneService airplaneService;

    @GetMapping("/airplane/{airplaneId}/airplane")
    public ResponseEntity<ApiResponse> getAirplaneById(@PathVariable Long airplaneId){
        try {
            AirplaneDTO airplane = airplaneService.getAirplaneById(airplaneId);
            return ResponseEntity.ok(new ApiResponse("Успешно", airplane));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/airplane/by/serial-number")
    public ResponseEntity<ApiResponse> getAirplaneBySerialNumber(@RequestParam String serialNum){
        try {
            AirplaneDTO airplane = airplaneService.getAirplaneBySerialNumber(serialNum);
            return ResponseEntity.ok(new ApiResponse("Успешно", airplane));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
