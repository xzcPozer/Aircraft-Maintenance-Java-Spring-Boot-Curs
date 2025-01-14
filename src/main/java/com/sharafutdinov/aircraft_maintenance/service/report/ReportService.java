package com.sharafutdinov.aircraft_maintenance.service.report;

import com.sharafutdinov.aircraft_maintenance.request.CreateReportRequest;
import net.sf.jasperreports.engine.JRException;

public interface ReportService {
    String exportReport(CreateReportRequest request, Long engineerId) throws JRException;
}
