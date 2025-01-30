package com.sharafutdinov.aircraft_maintenance.controller;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.request.AddScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.request.UpdateScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.response.ApiResponse;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;
import com.sharafutdinov.aircraft_maintenance.service.scheduled_check.ScheduledCheckService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/scheduled-checks")
@Tag(name = "ScheduledCheck")
public class ScheduledCheckController {

    private final ScheduledCheckService scheduledCheckService;

    @GetMapping("/all-scheduled-check")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<PageResponse<ScheduledCheckDTO>> getAllScheduledWorks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        PageResponse<ScheduledCheckDTO> scheduledChecks = scheduledCheckService.getAllScheduledChecks(page, size);
        return ResponseEntity.ok(scheduledChecks);
    }

    @GetMapping("/all-scheduled-check/by/date")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<PageResponse<ScheduledCheckDTO>> getAllScheduledWorkByDate(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) {
        PageResponse<ScheduledCheckDTO> scheduledChecks = scheduledCheckService.getAllScheduledChecksByDate(page, size, date);
        return ResponseEntity.ok(scheduledChecks);
    }

    @GetMapping("/my-scheduled-check")
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<PageResponse<ScheduledCheckDTO>> getAllScheduledAllCheckByEngineerId(
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
