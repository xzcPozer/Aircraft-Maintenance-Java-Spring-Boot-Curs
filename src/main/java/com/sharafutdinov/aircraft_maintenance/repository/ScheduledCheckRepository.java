package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ScheduledCheckRepository extends JpaRepository<ScheduledCheck, Long> {
    boolean existsByAirplane_SerialNumber(String serialNumber);

//    boolean existsByStatus(CheckStatus status);

    Page<ScheduledCheck> findByEngineerId(Pageable pageable, String engineerId);

    Page<ScheduledCheck> findAll(Pageable pageable);

    ScheduledCheck findByIdAndEngineerId(Long scheduledCheckId, String userId);

    @Query("""
            SELECT sc
            FROM ScheduledCheck sc
            WHERE DATE(sc.date) = :date
            """)
    Page<ScheduledCheck> findAllByDate(Pageable pageable, Date date);
}
