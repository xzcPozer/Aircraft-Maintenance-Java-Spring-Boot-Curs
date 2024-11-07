package com.sharafutdinov.aircraft_maintenance.service.scheduled_check;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTOMapper;
import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceAlreadyFoundException;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import com.sharafutdinov.aircraft_maintenance.repository.AirplaneRepository;
import com.sharafutdinov.aircraft_maintenance.repository.ScheduledCheckRepository;
import com.sharafutdinov.aircraft_maintenance.request. AddScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.request.UpdateScheduledCheckRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduledCheckServiceImpl implements ScheduledCheckService{
    private final ScheduledCheckRepository scheduledCheckRepository;
    private final ScheduledCheckDTOMapper scheduledCheckDTOMapper;
    private final AirplaneRepository airplaneRepository;

    @Override
    public ScheduledCheckDTO addScheduledCheck(AddScheduledCheckRequest scheduledCheckReq, Engineer engineer) {
        Airplane airplane = Optional.ofNullable(airplaneRepository.findBySerialNumber(scheduledCheckReq.getAirplaneSerialNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Такого самолета нет в БД"));

        return Optional.of(scheduledCheckReq)
                .filter(f -> !scheduledCheckRepository.existsByAirplane_SerialNumber(airplane.getSerialNumber())
                        && !scheduledCheckRepository.existsByStatus(CheckStatus.PLANNED))
                .map(req -> {
                    ScheduledCheck scheduledCheck = new ScheduledCheck();
                    scheduledCheck.setAirplane(airplane);
                    scheduledCheck.setType(scheduledCheckReq.getType());
                    scheduledCheck.setDate(scheduledCheckReq.getDate());
                    scheduledCheck.setStatus(scheduledCheckReq.getStatus());
                    scheduledCheck.setEngineer(engineer);
                    return scheduledCheckRepository.save(scheduledCheck);
                })
                .map(scheduledCheckDTOMapper)
                .orElseThrow(() -> new ResourceAlreadyFoundException("Этот самолет уже запланирован на проверку"));
    }

    @Override
    public ScheduledCheckDTO updateScheduledCheck(UpdateScheduledCheckRequest scheduledCheckReq, Long scheduledCheckId) {
        return scheduledCheckRepository
                .findById(scheduledCheckId)
                .map(updatedCheck -> {
                    updatedCheck.setDate(scheduledCheckReq.getDate());
                    updatedCheck.setStatus(scheduledCheckReq.getStatus());
                    return scheduledCheckRepository.save(updatedCheck);
                })
                .map(scheduledCheckDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Такой проверки не существует в БД"));
    }

    @Override
    public List<ScheduledCheckDTO> getAllScheduledCheckByEngineerId(Long engineerId) {
        List<ScheduledCheck> scheduledCheckList = Optional.ofNullable(scheduledCheckRepository.findByEngineerId(engineerId))
                .orElseThrow(() -> new ResourceNotFoundException("У этого инженера еще нет записей"));

        return scheduledCheckList
                .stream()
                .map(scheduledCheckDTOMapper)
                .toList();
    }

    @Override
    public ScheduledCheckDTO getScheduledCheckById(Long scheduledCheckId) {
        return scheduledCheckRepository.findById(scheduledCheckId)
                .map(scheduledCheckDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Такой проверки не существует в БД"));
    }


}
