package com.sharafutdinov.aircraft_maintenance.service.performed_work;

import com.sharafutdinov.aircraft_maintenance.dto.performed_work.AuthPerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.request.PerformedWorkRequest;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface PerformedWorkService {
    AuthPerformedWorkDTO addPerformedWork(PerformedWorkRequest request, String engineerId);
    AuthPerformedWorkDTO updatePerformedWork(PerformedWorkRequest request, Long performedWorkId, String userId);
    PageResponse<AuthPerformedWorkDTO> getAllPerformedWorksByEngineerId(int page, int size, String engineerId);
    List<AuthPerformedWorkDTO> getAllPerformedWorksByEngineerIdAndTime(String engineerId, LocalDate date);
    List<AuthPerformedWorkDTO> getAllPerformedWorksByEngineerIdAndTime(String engineerId, LocalDate date1, LocalDate date2);
    List<AuthPerformedWorkDTO> getAllPerformedWorksBySerialNumberAndTime(String serialNumber, LocalDate date);
    List<AuthPerformedWorkDTO> getAllPerformedWorksBySerialNumberAndTime(String serialNumber, LocalDate date1, LocalDate date2);
    PageResponse<PerformedWorkDTO> getAllPerformedWorks(int page, int size);
    AuthPerformedWorkDTO getWorkByIdAndEngineer(Long performedWorkId, String engineerId);
    PageResponse<AuthPerformedWorkDTO> getAllPerformedWorksByEngineerIdAndPeriod(int page, int size, String engineerId, Date date);
}
