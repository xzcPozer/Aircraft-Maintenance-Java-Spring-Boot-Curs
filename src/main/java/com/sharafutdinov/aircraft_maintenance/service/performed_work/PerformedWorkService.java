package com.sharafutdinov.aircraft_maintenance.service.performed_work;

import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import com.sharafutdinov.aircraft_maintenance.request.PerformedWorkRequest;

import java.util.List;

public interface PerformedWorkService {
    PerformedWorkDTO addPerformedWork(PerformedWorkRequest request, Engineer engineer);
    PerformedWorkDTO updatePerformedWork(PerformedWorkRequest request, Long performedWorkId);
    List<PerformedWorkDTO> getAllPerformedWorksByEngineerId(Long engineerId);
}
