package com.sharafutdinov.aircraft_maintenance.service.scheduled_task;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;

import java.util.List;

public interface ScheduledTaskService {
    void checkScheduledWork();

    void checkStatusScheduledWork();
}
