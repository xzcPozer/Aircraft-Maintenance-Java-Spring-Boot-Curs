package com.sharafutdinov.aircraft_maintenance.controller;

import com.sharafutdinov.aircraft_maintenance.dto.performed_work.AuthPerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.request.CreateReportByPeriodAndSerialNumberRequest;
import com.sharafutdinov.aircraft_maintenance.request.CreateReportByPeriodRequest;
import com.sharafutdinov.aircraft_maintenance.request.PerformedWorkRequest;
import com.sharafutdinov.aircraft_maintenance.response.ApiResponse;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;
import com.sharafutdinov.aircraft_maintenance.service.performed_work.PerformedWorkService;
import com.sharafutdinov.aircraft_maintenance.service.report.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/performed-works")
@Tag(name = "PerformedWork")
public class PerformedWorkController {
    private final PerformedWorkService performedWorkService;
    private final ReportService reportService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<PageResponse<PerformedWorkDTO>> getAllWorks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        PageResponse<PerformedWorkDTO> performedWorks = performedWorkService.getAllPerformedWorks(page, size);
        return ResponseEntity.ok(performedWorks);
    }

    @GetMapping("/my-performed-work")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<PageResponse<AuthPerformedWorkDTO>> getAllWorksByEngineerAuthId(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication authentication) {
        String engineerId = authentication.getName();

        PageResponse<AuthPerformedWorkDTO> performedWorks = performedWorkService.getAllPerformedWorksByEngineerId(page, size, engineerId);
        return ResponseEntity.ok(performedWorks);
    }

    @GetMapping("/my-performed-work/{performedWorkId}")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<ApiResponse> getWorkByIdAndByEngineerAuthId(
            @PathVariable Long performedWorkId,
            Authentication authentication){
        String engineerId = authentication.getName();

        AuthPerformedWorkDTO performedWork = performedWorkService.getWorkByIdAndEngineer(performedWorkId, engineerId);
        return ResponseEntity.ok(new ApiResponse("Успешно", performedWork));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<ApiResponse> createPerformedWork(
            @RequestBody @Valid PerformedWorkRequest request,
            Authentication authentication) {
        String engineerId = authentication.getName();

        AuthPerformedWorkDTO performedWork = performedWorkService.addPerformedWork(request, engineerId);
        return ResponseEntity.ok(new ApiResponse("Успешно", performedWork));
    }

    @PostMapping("/create-report/by/period")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<ApiResponse> createReportByPeriod(
            @RequestBody @Valid CreateReportByPeriodRequest request,
            JwtAuthenticationToken authentication) throws JRException {
        String reportInfo = reportService.exportReportByPeriod(request, authentication);
        return ResponseEntity.ok(new ApiResponse("Успешно", reportInfo));
    }

    @PostMapping("/create-report/by/period-and-serial-number")
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<ApiResponse> createReportByPeriodAndSerialNumber(
            @RequestBody @Valid CreateReportByPeriodAndSerialNumberRequest request,
            JwtAuthenticationToken authentication) throws JRException {
        String reportInfo = reportService.exportReportByPeriodAndSerialNumber(request, authentication);
        return ResponseEntity.ok(new ApiResponse("Успешно", reportInfo));
    }

    @PutMapping("/my-performed-work/change/{performedWorkId}")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<ApiResponse> updatePerformedWork(
            @RequestBody @Valid PerformedWorkRequest request,
            @PathVariable Long performedWorkId,
            Authentication authentication) {
        String userId = authentication.getName();

        AuthPerformedWorkDTO performedWork = performedWorkService.updatePerformedWork(request, performedWorkId, userId);
        return ResponseEntity.ok(new ApiResponse("Успешно", performedWork));
    }
}
