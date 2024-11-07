package com.sharafutdinov.aircraft_maintenance.service.scheduled_check;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import com.sharafutdinov.aircraft_maintenance.request.AddScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.request.UpdateScheduledCheckRequest;

import java.util.List;

public interface ScheduledCheckService {
    ScheduledCheckDTO addScheduledCheck(AddScheduledCheckRequest scheduledCheckReq, Engineer engineer);
    ScheduledCheckDTO updateScheduledCheck(UpdateScheduledCheckRequest scheduledCheckReq, Long scheduledCheckId);
    List<ScheduledCheckDTO> getAllScheduledCheckByEngineerId(Long engineerId);
    ScheduledCheckDTO getScheduledCheckById(Long scheduledCheckId);
}
