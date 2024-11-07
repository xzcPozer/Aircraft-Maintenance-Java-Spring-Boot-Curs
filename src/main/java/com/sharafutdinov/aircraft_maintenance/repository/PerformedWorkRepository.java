package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformedWorkRepository extends JpaRepository<PerformedWork, Long> {
    boolean existsByWorkName(String workName);
    List<PerformedWork> findByEngineerId(Long engineerId);
}
