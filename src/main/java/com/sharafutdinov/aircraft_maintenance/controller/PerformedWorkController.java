package com.sharafutdinov.aircraft_maintenance.controller;

import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.exceptions.GenerateException;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceAlreadyFoundException;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.request.CreateReportRequest;
import com.sharafutdinov.aircraft_maintenance.request.PerformedWorkRequest;
import com.sharafutdinov.aircraft_maintenance.response.ApiResponse;
import com.sharafutdinov.aircraft_maintenance.security.jwt.JWTService;
import com.sharafutdinov.aircraft_maintenance.service.engineer.EngineerService;
import com.sharafutdinov.aircraft_maintenance.service.performed_work.PerformedWorkService;
import com.sharafutdinov.aircraft_maintenance.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/performed-works")
public class PerformedWorkController {
    private final PerformedWorkService performedWorkService;
    private final EngineerService engineerService;
    private final ReportService reportService;
    private final JWTService jwtService;

    @GetMapping("/my-performed-work")
    public ResponseEntity<ApiResponse> getAllWorksByEngineerId(@RequestHeader("Authorization") String token){
        try {
            Long engineerId = jwtService.getIdFromToken(token);
            List<PerformedWorkDTO> performedWorks = performedWorkService.getAllPerformedWorksByEngineerId(engineerId);
            return ResponseEntity.ok(new ApiResponse("Успешно", performedWorks));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createPerformedWork(@RequestBody PerformedWorkRequest request){
        try {
            Engineer engineer = engineerService.getAuthenticatedEngineer();

            PerformedWorkDTO performedWork = performedWorkService.addPerformedWork(request, engineer);
            return ResponseEntity.ok(new ApiResponse("Успешно", performedWork));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        catch (ResourceAlreadyFoundException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create-report")
    public ResponseEntity<ApiResponse> createReport(@RequestHeader("Authorization") String token, @RequestBody CreateReportRequest request){
        try {
            Long engineerId = jwtService.getIdFromToken(token);

            String reportInfo = reportService.exportReport(request, engineerId);
            return ResponseEntity.ok(new ApiResponse("Успешно", reportInfo));
        } catch (JRException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        } catch (GenerateException | ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/performed-work/{performedWorkId}/update")
    public ResponseEntity<ApiResponse> updatePerformedWork(@RequestBody PerformedWorkRequest request, @PathVariable Long performedWorkId){
        try {
            PerformedWorkDTO performedWork = performedWorkService.updatePerformedWork(request, performedWorkId);
            return ResponseEntity.ok(new ApiResponse("Успешно", performedWork));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        catch (ResourceAlreadyFoundException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
