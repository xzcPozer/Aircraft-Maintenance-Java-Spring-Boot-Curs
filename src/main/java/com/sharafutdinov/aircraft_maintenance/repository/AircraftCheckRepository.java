package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.model.AircraftCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftCheckRepository extends JpaRepository<AircraftCheck, Long> {
    AircraftCheck findByCheckName(String checkName);

    Page<AircraftCheck> findAll(Pageable pageable);

    boolean existsByCheckName(String checkName);
}
