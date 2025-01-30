package com.sharafutdinov.aircraft_maintenance.service.aircraft_check;

import com.sharafutdinov.aircraft_maintenance.model.AircraftCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AircraftService {

    List<String> findAllAircraftCheck();
}
