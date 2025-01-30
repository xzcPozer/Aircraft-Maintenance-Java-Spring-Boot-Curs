package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PerformedWorkRepository extends JpaRepository<PerformedWork, Long> {
    List<PerformedWork> findByEngineerId(String engineerId);
    List<PerformedWork> findByAirplane_SerialNumber(String serialNumber);
    Page<PerformedWork> findByEngineerId(Pageable pageable, String engineerId);
    Page<PerformedWork> findAll(Pageable pageable);
    PerformedWork findByIdAndEngineerId(Long performedWorkId, String engineerId);

    @Query("""
            SELECT pw
            FROM PerformedWork pw
            WHERE pw.engineerId = :engineerId
            AND DATE(pw.completionDate) = :date
            """)
    Page<PerformedWork> findByEngineerIdAndCompletionDate(Pageable pageable, String engineerId, Date date);
}
