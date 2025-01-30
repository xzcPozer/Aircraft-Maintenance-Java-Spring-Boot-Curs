package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    Airplane findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);
    Page<Airplane> findAll(Pageable pageable);
}
