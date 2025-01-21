package com.sharafutdinov.aircraft_maintenance.service.scheduled_task;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTOMapper;
import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.model.ScheduledCheck;
import com.sharafutdinov.aircraft_maintenance.repository.ScheduledCheckRepository;
import com.sharafutdinov.aircraft_maintenance.request.SendEmailRequest;
import com.sharafutdinov.aircraft_maintenance.service.email.EmailService;
import com.sharafutdinov.aircraft_maintenance.service.notification.NotificationService;
import jakarta.mail.MessagingException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ScheduledCheckRepository scheduledCheckRepository;
    private final ScheduledCheckDTOMapper scheduledCheckDTOMapper;
    private final Keycloak keycloakAdminClient;
    private final NotificationService notificationService;
    private final KeycloakService keycloakService;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.clientMail}")
    private String seniorEngineerMail;

    @Scheduled(cron = "${task.cron.checkScheduledWork}", zone = "Europe/Moscow")
    @Transactional
    @Override
    public void sendScheduledWorks() {
        List<ScheduledCheck> scheduledWorks = scheduledCheckRepository.findAll();

        List<ScheduledCheckDTO> scheduledWorksDTO = scheduledWorks
                .stream()
                .filter(scheduledCheck -> scheduledCheck.getStatus().equals(CheckStatus.PLANNED)
                        && ChronoUnit.HOURS.between(scheduledCheck.getDate(), LocalDateTime.now()) <= 24
                        && !scheduledCheck.isNotificationSent())
                .map(sc -> {
                    sc.setNotificationSent(true);
                    return scheduledCheckRepository.save(sc);
                })
                .map(scheduledCheckDTOMapper)
                .toList();

        if (scheduledWorksDTO.isEmpty())
            return;

        Map<UserRepresentation, List<RoleRepresentation>> usersKeycloak = keycloakService.getUsersWithRoles("ENGINEER");

        List<UserRepresentation> engineers = keycloakService.getUserRepresentation(usersKeycloak);
        for (ScheduledCheckDTO work : scheduledWorksDTO) {
            for (UserRepresentation engineer : engineers) {
                if (engineer.getEmail() != null) {
                    try {
                        notificationService.sendNotification(SendEmailRequest.builder()
                                        .scheduledCheckDTO(work)
                                        .from(seniorEngineerMail)
                                        .to(engineer.getEmail())
                                        .build()
                                , false);
                    } catch (Exception e) {
                        log.error("Failed to send notification to user {}", engineer.getId(), e);
                        throw new RuntimeException("Failed to send notification", e);
                    }
                }
                // todo: не забыть раскомментировать для frontend
                //simpMessagingTemplate.convertAndSendToUser(engineer.getId(), "/notifications", work);
            }
        }
    }

    @Transactional
    @Override
    public void sendUpdatedScheduledWorks(ScheduledCheck scheduledCheck) {

        scheduledCheck.setNotificationSent(true);
        scheduledCheckRepository.save(scheduledCheck);

        Map<UserRepresentation, List<RoleRepresentation>> usersKeycloak = keycloakService.getUsersWithRoles("ENGINEER");

        List<UserRepresentation> engineers = keycloakService.getUserRepresentation(usersKeycloak);

        for (UserRepresentation engineer : engineers) {
            ScheduledCheckDTO dto = new ScheduledCheckDTO(scheduledCheck.getType(),
                    scheduledCheck.getDate(),
                    scheduledCheck.getStatus(),
                    scheduledCheck.getAirplane().getSerialNumber());
            if (engineer.getEmail() != null) {
                try {
                    notificationService.sendNotification(SendEmailRequest.builder()
                                    .scheduledCheckDTO(dto)
                                    .from(seniorEngineerMail)
                                    .to(engineer.getEmail())
                                    .build(),
                            true);
                } catch (Exception e) {
                    log.error("Failed to send notification to user {}", engineer.getId(), e);
                    throw new RuntimeException("Failed to send notification", e);
                }
            }
            // todo: не забыть раскомментировать для frontend
            //simpMessagingTemplate.convertAndSendToUser(engineer.getId(), "/notifications", dto);
        }

    }
}
