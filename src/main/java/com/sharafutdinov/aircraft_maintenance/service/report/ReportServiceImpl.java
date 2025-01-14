package com.sharafutdinov.aircraft_maintenance.service.report;

import com.sharafutdinov.aircraft_maintenance.dto.engineer.EngineerDTO;
import com.sharafutdinov.aircraft_maintenance.dto.engineer.EngineerForReport;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.exceptions.GenerateException;
import com.sharafutdinov.aircraft_maintenance.request.CreateReportRequest;
import com.sharafutdinov.aircraft_maintenance.service.engineer.EngineerService;
import com.sharafutdinov.aircraft_maintenance.service.performed_work.PerformedWorkService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final PerformedWorkService performedWorkService;
    private final EngineerService engineerService;

    private static final String path =  "classpath:\\report";
    private static final String jasperFilePath =  "D:\\Java Spring\\aircraft_maintenance\\src\\main\\resources\\report\\EngineerPerformedWorks.jrxml";
    private static final String jasperOutPath = "D:\\Java Spring\\aircraft_maintenance\\src\\main\\resources\\report\\";

    @Override
    public String exportReport(CreateReportRequest request, Long engineerId ) throws JRException {

        EngineerDTO engineer = engineerService.getEngineerById(engineerId);

        // проверка на ввод даты
        EngineerForReport engineerForReport;
        List<PerformedWorkDTO> performedWorks;
        if(request.getDate1() == null || request.getDate1().isAfter(request.getDate2()))
            throw new GenerateException("Неправильно указанная дата");
        else if (request.getDate2() == null || request.getDate1().equals(request.getDate2())) {
            engineerForReport = new EngineerForReport(engineer, request.getDate1());

            performedWorks = performedWorkService.getAllPerformedWorksByEngineerIdAndTime(engineerId,request.getDate1());
        }
        else{
            engineerForReport = new EngineerForReport(engineer, request.getDate1(), request.getDate2());

            performedWorks = performedWorkService.getAllPerformedWorksByEngineerIdAndTime(engineerId,request.getDate1(), request.getDate2());
        }

        List<EngineerForReport> engineerForReportList = List.of(engineerForReport);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperFilePath);;

        JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(performedWorks);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(engineerForReportList);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("TABLE_DATA_SOURCE", tableDataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        if (request.getReportFormat().equals("xml")) {
            JasperExportManager.exportReportToXmlFile(jasperPrint, jasperOutPath + "performed-works.xml", false);
        }
        if (request.getReportFormat().equals("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, jasperOutPath + "performed-works.pdf");
        }

        return "report generated in path : " + path;
    }
}
