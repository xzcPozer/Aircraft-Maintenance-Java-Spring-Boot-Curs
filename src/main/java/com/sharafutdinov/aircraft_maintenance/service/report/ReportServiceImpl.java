package com.sharafutdinov.aircraft_maintenance.service.report;

import com.sharafutdinov.aircraft_maintenance.dto.engineer.EngineerForReport;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.AuthPerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.exceptions.GenerateException;
import com.sharafutdinov.aircraft_maintenance.request.CreateReportByPeriodAndSerialNumberRequest;
import com.sharafutdinov.aircraft_maintenance.request.CreateReportByPeriodRequest;
import com.sharafutdinov.aircraft_maintenance.service.performed_work.PerformedWorkService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final PerformedWorkService performedWorkService;

    private static String jasperFilePath;

    @PostConstruct
    public void init() {
        try {
            jasperFilePath = new ClassPathResource("report/EngineerPerformedWorks.jrxml").getFile().getAbsolutePath();
        } catch (IOException e) {
            log.error("Не удалось загрузить файл Jasper", e);
        }
    }

    @Override
    public byte[] exportReportByPeriod(CreateReportByPeriodRequest request, JwtAuthenticationToken authentication) throws JRException {

        var jwt = authentication.getToken();

        // проверка на ввод даты
        EngineerForReport engineerForReport;
        List<AuthPerformedWorkDTO> performedWorks;

        if (request.getDate1() == null)
            throw new GenerateException("Неправильно указанная дата");
        else if (request.getDate2() != null) {
            if (request.getDate1().isAfter(request.getDate2())) {
                throw new GenerateException("Неправильно указанная дата");
            }
        }

        if (request.getDate2() == null || request.getDate1().equals(request.getDate2())) {
            engineerForReport = new EngineerForReport(authentication, request.getDate1());

            performedWorks = performedWorkService.getAllPerformedWorksByEngineerIdAndTime(jwt.getSubject(), request.getDate1());
        } else {
            engineerForReport = new EngineerForReport(authentication, request.getDate1(), request.getDate2());

            performedWorks = performedWorkService.getAllPerformedWorksByEngineerIdAndTime(jwt.getSubject(), request.getDate1(), request.getDate2());
        }

        List<EngineerForReport> engineerForReportList = List.of(engineerForReport);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperFilePath);

        JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(performedWorks);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(engineerForReportList);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("TABLE_DATA_SOURCE", tableDataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (request.getReportFormat().equals("xml")) {
            JasperExportManager.exportReportToXmlStream(jasperPrint, baos);
        }
        if (request.getReportFormat().equals("pdf")) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        }

        return baos.toByteArray();
    }

    @Override
    public byte[] exportReportByPeriodAndSerialNumber(CreateReportByPeriodAndSerialNumberRequest request, JwtAuthenticationToken authentication) throws JRException {

        var jwt = authentication.getToken();

        // проверка на ввод даты
        EngineerForReport engineerForReport;
        List<AuthPerformedWorkDTO> performedWorks;
        if (request.getDate1() == null)
            throw new GenerateException("Неправильно указанная дата");
        else if (request.getDate2() != null) {
            if (request.getDate1().isAfter(request.getDate2())) {
                throw new GenerateException("Неправильно указанная дата");
            }
        }

        if (request.getDate2() == null || request.getDate1().equals(request.getDate2())) {
            engineerForReport = new EngineerForReport(authentication, request.getDate1());

            performedWorks = performedWorkService.getAllPerformedWorksBySerialNumberAndTime(request.getSerialNumber(), request.getDate1());
        } else {
            engineerForReport = new EngineerForReport(authentication, request.getDate1(), request.getDate2());

            performedWorks = performedWorkService.getAllPerformedWorksBySerialNumberAndTime(request.getSerialNumber(), request.getDate1(), request.getDate2());
        }

        List<EngineerForReport> engineerForReportList = List.of(engineerForReport);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperFilePath);

        JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(performedWorks);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(engineerForReportList);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("TABLE_DATA_SOURCE", tableDataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (request.getReportFormat().equals("xml")) {
            JasperExportManager.exportReportToXmlStream(jasperPrint, baos);
        }
        if (request.getReportFormat().equals("pdf")) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        }

        return baos.toByteArray();
    }
}
