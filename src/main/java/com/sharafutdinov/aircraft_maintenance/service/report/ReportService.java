package com.sharafutdinov.aircraft_maintenance.service.report;

import com.sharafutdinov.aircraft_maintenance.request.CreateReportByPeriodAndSerialNumberRequest;
import com.sharafutdinov.aircraft_maintenance.request.CreateReportByPeriodRequest;
import net.sf.jasperreports.engine.JRException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface ReportService {
    byte[] exportReportByPeriod(CreateReportByPeriodRequest request, JwtAuthenticationToken authentication) throws JRException;
    byte[] exportReportByPeriodAndSerialNumber(CreateReportByPeriodAndSerialNumberRequest request, JwtAuthenticationToken authentication) throws JRException;
}
