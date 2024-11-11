package com.jenesano.directory.controller;

import com.jenesano.directory.dto.ReportDTO;
import com.jenesano.directory.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<ReportDTO> generateReport() {
        ReportDTO reportDTO = reportService.generateReport();
        return ResponseEntity.ok(reportDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/pdf")
    public ResponseEntity<String> generateReportPdf() {
        try {
            String pdfUrl = reportService.generateReportPdf();
            return ResponseEntity.ok(pdfUrl);
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.status(500).body("Error generando el PDF: " + e.getMessage());
        }
    }
}
