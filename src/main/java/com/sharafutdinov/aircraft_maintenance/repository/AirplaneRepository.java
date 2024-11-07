package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    Airplane findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);
}
