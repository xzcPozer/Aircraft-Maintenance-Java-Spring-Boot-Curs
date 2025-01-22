package com.sharafutdinov.aircraft_maintenance.service.scheduled_check;

import com.sharafutdinov.aircraft_maintenance.dto.performed_work.AuthPerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTOMapper;
import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceAlreadyFoundException;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import com.sharafutdinov.aircraft_maintenance.repository.AirplaneRepository;
import com.sharafutdinov.aircraft_maintenance.repository.ScheduledCheckRepository;
import com.sharafutdinov.aircraft_maintenance.request.AddScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.request.SendEmailRequest;
import com.sharafutdinov.aircraft_maintenance.request.UpdateScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;
import com.sharafutdinov.aircraft_maintenance.service.scheduled_task.ScheduledTaskService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduledCheckServiceImpl implements ScheduledCheckService {
    private final ScheduledCheckRepository scheduledCheckRepository;
    private final ScheduledCheckDTOMapper scheduledCheckDTOMapper;
    private final AirplaneRepository airplaneRepository;
    private final ScheduledTaskService scheduledTaskService;

    @Override
    public ScheduledCheckDTO addScheduledCheck(AddScheduledCheckRequest scheduledCheckReq, String engineerId) {
        Airplane airplane = Optional.ofNullable(airplaneRepository.findBySerialNumber(scheduledCheckReq.getAirplaneSerialNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Такого самолета нет в БД"));

        return Optional.of(scheduledCheckReq)
                .filter(f -> !scheduledCheckRepository
                        .existsByAirplane_SerialNumber(airplane.getSerialNumber()))
                .map(req -> {
                    ScheduledCheck scheduledCheck = new ScheduledCheck();
                    scheduledCheck.setAirplane(airplane);
                    scheduledCheck.setType(scheduledCheckReq.getType());
                    scheduledCheck.setDate(scheduledCheckReq.getDate());
                    scheduledCheck.setStatus(scheduledCheckReq.getStatus());
                    scheduledCheck.setEngineerId(engineerId);
                    scheduledCheck.setNotificationSent(false);
                    return scheduledCheckRepository.save(scheduledCheck);
                })
                .map(scheduledCheckDTOMapper)
                .orElseThrow(() -> new ResourceAlreadyFoundException("Этот самолет уже запланирован на проверку"));
    }

    @Override
    public ScheduledCheckDTO updateScheduledCheck(UpdateScheduledCheckRequest scheduledCheckReq, Long scheduledCheckId, String userId) throws MessagingException {
        ScheduledCheck sc = Optional.ofNullable(scheduledCheckRepository
                .findByIdAndEngineerId(scheduledCheckId, userId))
                .map(updatedCheck -> {
                    updatedCheck.setDate(scheduledCheckReq.getDate());
                    updatedCheck.setStatus(scheduledCheckReq.getStatus());
                    updatedCheck.setNotificationSent(false);
                    return scheduledCheckRepository.save(updatedCheck);
                })
                .orElseThrow(() -> new ResourceNotFoundException("У вас нет такой проверки в БД"));

        scheduledTaskService.sendUpdatedScheduledWorks(sc);

        return scheduledCheckDTOMapper.apply(sc);
    }

    @Override
    public PageResponse<ScheduledCheckDTO> getAllScheduledCheckByEngineerId(int page, int size, String engineerId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<ScheduledCheck> scheduledCheckList = Optional.ofNullable(scheduledCheckRepository.findByEngineerId(pageable, engineerId))
                .orElseThrow(() -> new ResourceNotFoundException("У этого инженера еще нет записей"));
        List<ScheduledCheckDTO> scheduledChecksResponse = scheduledCheckList.stream()
                .map(scheduledCheckDTOMapper)
                .toList();

        return new PageResponse<>(
                scheduledChecksResponse,
                scheduledCheckList.getNumber(),
                scheduledCheckList.getSize(),
                scheduledCheckList.getTotalElements(),
                scheduledCheckList.getTotalPages(),
                scheduledCheckList.isFirst(),
                scheduledCheckList.isLast()
        );
    }

    @Override
    public ScheduledCheckDTO getScheduledCheckById(Long scheduledCheckId) {
        return scheduledCheckRepository.findById(scheduledCheckId)
                .map(scheduledCheckDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Такой проверки не существует в БД"));
    }

    @Override
    public PageResponse<ScheduledCheckDTO> getAllScheduledChecks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<ScheduledCheck> scheduledChecks = scheduledCheckRepository.findAll(pageable);
        List<ScheduledCheckDTO> scheduledChecksResponse = scheduledChecks.stream()
                .map(scheduledCheckDTOMapper)
                .toList();

        return new PageResponse<>(
                scheduledChecksResponse,
                scheduledChecks.getNumber(),
                scheduledChecks.getSize(),
                scheduledChecks.getTotalElements(),
                scheduledChecks.getTotalPages(),
                scheduledChecks.isFirst(),
                scheduledChecks.isLast()
        );
    }

    @Override
    public PageResponse<ScheduledCheckDTO> getAllScheduledChecksByDate(int page, int size, Date date) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ScheduledCheck> scheduledChecks = scheduledCheckRepository.findAllByDate(pageable, date);
        List<ScheduledCheckDTO> scheduledChecksResponse = scheduledChecks.stream()
                .map(scheduledCheckDTOMapper)
                .toList();

        return new PageResponse<>(
                scheduledChecksResponse,
                scheduledChecks.getNumber(),
                scheduledChecks.getSize(),
                scheduledChecks.getTotalElements(),
                scheduledChecks.getTotalPages(),
                scheduledChecks.isFirst(),
                scheduledChecks.isLast()
        );
    }
}
