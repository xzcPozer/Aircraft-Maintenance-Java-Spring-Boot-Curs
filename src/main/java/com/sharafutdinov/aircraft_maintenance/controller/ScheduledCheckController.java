package com.sharafutdinov.aircraft_maintenance.controller;

import com.sharafutdinov.aircraft_maintenance.dto.performed_work.AuthPerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceAlreadyFoundException;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.request.AddScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.request.UpdateScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.response.ApiResponse;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;
import com.sharafutdinov.aircraft_maintenance.service.scheduled_check.ScheduledCheckService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/scheduled-checks")
@Tag(name = "ScheduledCheck")
public class ScheduledCheckController {

    private final ScheduledCheckService scheduledCheckService;

    @GetMapping("/all-scheduled-check")
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<PageResponse<ScheduledCheckDTO>> getAllScheduledWorks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        PageResponse<ScheduledCheckDTO> scheduledChecks = scheduledCheckService.getAllScheduledChecks(page, size);
        return ResponseEntity.ok(scheduledChecks);
    }

    @GetMapping("/my-scheduled-check")
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<PageResponse<ScheduledCheckDTO>> getAllScheduledCheckByEngineerId(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication authentication) {
        String engineerId = authentication.getName();
        PageResponse<ScheduledCheckDTO> scheduledChecks = scheduledCheckService.getAllScheduledCheckByEngineerId(page, size, engineerId);
        return ResponseEntity.ok(scheduledChecks);
    }

    @GetMapping("/scheduled-check/{scheduledCheckId}")
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<ScheduledCheckDTO> getScheduledCheckById(@PathVariable Long scheduledCheckId) {
        ScheduledCheckDTO scheduledCheck = scheduledCheckService.getScheduledCheckById(scheduledCheckId);
        return ResponseEntity.ok(scheduledCheck);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<ApiResponse> addScheduledCheck(
            @RequestBody  @Valid AddScheduledCheckRequest request,
            Authentication authentication) {
        String engineerId = authentication.getName();

        ScheduledCheckDTO scheduledCheck = scheduledCheckService.addScheduledCheck(request, engineerId);
        return ResponseEntity.ok(new ApiResponse("Успешно", scheduledCheck));
    }

    @PutMapping("/scheduled-check/change/{scheduledCheckId}")
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<ApiResponse> updateScheduledCheck(
            @RequestBody  @Valid UpdateScheduledCheckRequest request,
            @PathVariable Long scheduledCheckId,
            Authentication authentication) throws MessagingException {
        String userId = authentication.getName();

        ScheduledCheckDTO scheduledCheck = scheduledCheckService.updateScheduledCheck(request, scheduledCheckId, userId);
        return ResponseEntity.ok(new ApiResponse("Успешно", scheduledCheck));
    }
}
