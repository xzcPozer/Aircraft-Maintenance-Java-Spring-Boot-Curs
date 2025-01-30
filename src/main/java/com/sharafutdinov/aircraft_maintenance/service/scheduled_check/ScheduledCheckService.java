package com.sharafutdinov.aircraft_maintenance.service.scheduled_check;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.request.AddScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.request.UpdateScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;
import jakarta.mail.MessagingException;

import java.util.Date;

public interface ScheduledCheckService {
    ScheduledCheckDTO addScheduledCheck(AddScheduledCheckRequest scheduledCheckReq, String engineerId);
    ScheduledCheckDTO updateScheduledCheck(UpdateScheduledCheckRequest scheduledCheckReq, Long scheduledCheckId, String userId) throws MessagingException;
    PageResponse<ScheduledCheckDTO> getAllScheduledCheckByEngineerId(int page, int size, String engineerId);
    ScheduledCheckDTO getScheduledCheckById(Long scheduledCheckId);
    PageResponse<ScheduledCheckDTO> getAllScheduledChecks(int page, int size);
    PageResponse<ScheduledCheckDTO> getAllScheduledChecksByDate(int page, int size, Date date);
}
