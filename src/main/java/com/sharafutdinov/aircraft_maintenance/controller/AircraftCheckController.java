package com.sharafutdinov.aircraft_maintenance.controller;

import com.sharafutdinov.aircraft_maintenance.service.aircraft_check.AircraftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/aircraft-check")
@Tag(name = "AircraftCheck")
public class AircraftCheckController {

    private final AircraftService service;

    @GetMapping
    public ResponseEntity<List<String>> getAllChecks(){
        return ResponseEntity.ok(service.findAllAircraftCheck());
    }
}
