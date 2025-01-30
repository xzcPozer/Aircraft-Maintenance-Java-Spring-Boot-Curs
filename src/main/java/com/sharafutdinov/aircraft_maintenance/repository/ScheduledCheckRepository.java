package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface ScheduledCheckRepository extends JpaRepository<ScheduledCheck, Long> {

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
