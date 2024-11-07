package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduledCheckRepository extends JpaRepository<ScheduledCheck, Long> {
    boolean existsByAirplane_SerialNumber(String serialNumber);
    boolean existsByStatus(CheckStatus status);
    List<ScheduledCheck> findByEngineerId(Long engineerId);
}
