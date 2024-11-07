package com.sharafutdinov.aircraft_maintenance.controller;

import com.sharafutdinov.aircraft_maintenance.dto.scheduled_check.ScheduledCheckDTO;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceAlreadyFoundException;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.request.AddScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.request.UpdateScheduledCheckRequest;
import com.sharafutdinov.aircraft_maintenance.response.ApiResponse;
import com.sharafutdinov.aircraft_maintenance.security.jwt.JWTService;
import com.sharafutdinov.aircraft_maintenance.service.engineer.EngineerService;
import com.sharafutdinov.aircraft_maintenance.service.scheduled_check.ScheduledCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/scheduled-checks")
public class ScheduledCheckController {
    private final ScheduledCheckService scheduledCheckService;
    private final EngineerService engineerService;
    private final JWTService jwtService;

    @GetMapping("/my-scheduled-check")
    public ResponseEntity<ApiResponse> getAllScheduledCheckByEngineerId(@RequestHeader("Authorization") String token) {
        try {
            Long engineerId = jwtService.getIdFromToken(token);
            List<ScheduledCheckDTO> scheduledChecks = scheduledCheckService.getAllScheduledCheckByEngineerId(engineerId);
            return ResponseEntity.ok(new ApiResponse("Успешно", scheduledChecks));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
        }
    }

    @MessageMapping("/my-scheduled-check")
    @SendTo("/app/my-scheduled-check")
    public ResponseEntity<ApiResponse> send(Message message) {
        return Optional.ofNullable(message)
                .map(m -> ResponseEntity.ok(new ApiResponse("Успешно", m)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/scheduled-check/{scheduledCheckId}/scheduled-check")
    public ResponseEntity<ApiResponse> getScheduledCheckById(@PathVariable Long scheduledCheckId) {
        try {
            ScheduledCheckDTO scheduledCheck = scheduledCheckService.getScheduledCheckById(scheduledCheckId);
            return ResponseEntity.ok(new ApiResponse("Успешно", scheduledCheck));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addScheduledCheck(@RequestBody AddScheduledCheckRequest request) {
        try {
            Engineer engineer = engineerService.getAuthenticatedEngineer();

            ScheduledCheckDTO scheduledCheck = scheduledCheckService.addScheduledCheck(request, engineer);
            return ResponseEntity.ok(new ApiResponse("Успешно", scheduledCheck));
        } catch (ResourceAlreadyFoundException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/scheduled-check/{scheduledCheckId}/update")
    public ResponseEntity<ApiResponse> updateScheduledCheck(@RequestBody UpdateScheduledCheckRequest request,
                                                            @PathVariable Long scheduledCheckId) {
        try {
            ScheduledCheckDTO scheduledCheck = scheduledCheckService.updateScheduledCheck(request, scheduledCheckId);
            return ResponseEntity.ok(new ApiResponse("Успешно", scheduledCheck));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
