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
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Date;

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

    @GetMapping("/my-performed-work/by/period")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<PageResponse<AuthPerformedWorkDTO>> getAllPerformedWorksByEngineerAuthIdAndDate(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            Authentication authentication) {
        String engineerId = authentication.getName();

        PageResponse<AuthPerformedWorkDTO> performedWorks = performedWorkService.getAllPerformedWorksByEngineerIdAndPeriod(page, size, engineerId, date);
        return ResponseEntity.ok(performedWorks);
    }

    @GetMapping("/my-performed-work/{performedWorkId}")
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<AuthPerformedWorkDTO> getWorkByIdAndByEngineerAuthId(
            @PathVariable Long performedWorkId,
            Authentication authentication){
        String engineerId = authentication.getName();

        AuthPerformedWorkDTO performedWork = performedWorkService.getWorkByIdAndEngineer(performedWorkId, engineerId);
        return ResponseEntity.ok(performedWork);
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
    public ResponseEntity<InputStreamResource> createReportByPeriod(
            @RequestBody @Valid CreateReportByPeriodRequest request,
            JwtAuthenticationToken authentication) throws JRException {
        MediaType mediaType;
        String fileName;

        if (request.getReportFormat().equals("xml")) {
            mediaType = MediaType.APPLICATION_XML;
            fileName = "engineer-performed-works.xml";
        } else {
            mediaType = MediaType.APPLICATION_PDF;
            fileName = "engineer-performed-works.pdf";
        }

        byte[] reportInfo = reportService.exportReportByPeriod(request, authentication);

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(reportInfo));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        // Возврат ответа с файлом
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .body(resource);
    }

    @PostMapping("/create-report/by/period-and-serial-number")
    @PreAuthorize("hasRole('ROLE_SENIOR_ENGINEER')")
    public ResponseEntity<InputStreamResource> createReportByPeriodAndSerialNumber(
            @RequestBody @Valid CreateReportByPeriodAndSerialNumberRequest request,
            JwtAuthenticationToken authentication) throws JRException {
        MediaType mediaType;
        String fileName;

        if (request.getReportFormat().equals("xml")) {
            mediaType = MediaType.APPLICATION_XML;
            fileName = "all-performed-works.xml";
        } else {
            mediaType = MediaType.APPLICATION_PDF;
            fileName = "all-performed-works.pdf";
        }

        byte[] reportInfo = reportService.exportReportByPeriodAndSerialNumber(request, authentication);

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(reportInfo));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        // Возврат ответа с файлом
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .body(resource);
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
