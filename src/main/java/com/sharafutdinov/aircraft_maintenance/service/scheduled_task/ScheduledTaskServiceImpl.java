package com.sharafutdinov.aircraft_maintenance.service.scheduled_task;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTOMapper;
import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import com.sharafutdinov.aircraft_maintenance.repository.ScheduledCheckRepository;
import com.sharafutdinov.aircraft_maintenance.service.engineer.EngineerService;
import com.sharafutdinov.aircraft_maintenance.service.scheduled_check.ScheduledCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ScheduledCheckRepository scheduledCheckRepository;
    private final ScheduledCheckDTOMapper scheduledCheckDTOMapper;
    private final EngineerService engineerService;

    @Scheduled(cron = "${task.cron.checkScheduledWork}", zone = "Europe/Moscow")
    @Override
    public void checkScheduledWork() {
        Engineer engineer = engineerService.getAuthenticatedEngineer();

        if (engineer == null) {return;}

        List<ScheduledCheck> scheduledWorks = Optional.ofNullable(scheduledCheckRepository.findByEngineerId(engineer.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("У инженера %s нет запланированных проверок", engineer.getUsername())));

        List<ScheduledCheckDTO> scheduledWorksDTO = scheduledWorks
                .stream()
                .filter(scheduledCheck -> scheduledCheck.getStatus().equals(CheckStatus.PLANNED)
                        && ChronoUnit.HOURS.between(scheduledCheck.getDate(), LocalDateTime.now()) <= 24)
                .map(scheduledCheckDTOMapper)
                .toList();

        simpMessagingTemplate.convertAndSendToUser(engineer.getUsername(),"/main/my-scheduled-check", scheduledWorksDTO);

    }

    @Scheduled(cron = "${task.cron.checkStatusScheduledWork}", zone = "Europe/Moscow")
    @Override
    public void checkStatusScheduledWork() {
        Engineer engineer = engineerService.getAuthenticatedEngineer();

        if (engineer == null) {return;}

        List<ScheduledCheck> scheduledWorks = Optional.ofNullable(scheduledCheckRepository.findByEngineerId(engineer.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("У инженера %s нет запланированных проверок", engineer.getUsername())));

        scheduledWorks
                .stream()
                .filter(scheduledCheck -> scheduledCheck.getStatus().equals(CheckStatus.PLANNED)
                        && ChronoUnit.HOURS.between(scheduledCheck.getDate(), LocalDateTime.now()) < 0)
                .peek(updatedScheduledWork -> updatedScheduledWork.setStatus(CheckStatus.CANCELED))
                .forEach(scheduledCheckRepository::save);
    }
}
