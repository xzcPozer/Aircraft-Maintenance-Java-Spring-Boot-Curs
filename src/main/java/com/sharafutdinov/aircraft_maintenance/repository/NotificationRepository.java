package com.sharafutdinov.aircraft_maintenance.repository;

import com.sharafutdinov.aircraft_maintenance.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
