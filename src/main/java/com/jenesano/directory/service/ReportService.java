package com.jenesano.directory.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.jenesano.directory.dto.ReportDTO;
import com.jenesano.directory.entity.Page;
import com.jenesano.directory.entity.TypeBusiness;
import com.jenesano.directory.entity.Visit;
import com.jenesano.directory.repository.TypeBusinessRepository;
import com.jenesano.directory.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class ReportService {

    private final VisitRepository visitRepository;
    private final TypeBusinessRepository typeBusinessRepository;
    private final FileUploadService fileUploadService;

    @Autowired
    public ReportService(VisitRepository visitRepository, TypeBusinessRepository typeBusinessRepository, FileUploadService fileUploadService) {
        this.visitRepository = visitRepository;
        this.typeBusinessRepository = typeBusinessRepository;
        this.fileUploadService = fileUploadService;
    }

    public void recordBusinessVisit(Long typeBusinessId) {
        Visit visit = new Visit(Page.BUSINESS, typeBusinessId, LocalDateTime.now());
        visitRepository.save(visit);
    }

    public void recordEventVisit() {
        Visit visit = new Visit(Page.EVENT, LocalDateTime.now());
        visitRepository.save(visit);
    }

    public void recordTouristPlaceVisit() {
        Visit visit = new Visit(Page.TOURIST_PLACE, LocalDateTime.now());
        visitRepository.save(visit);
    }

    public ReportDTO generateReport() {
        ReportDTO report = new ReportDTO();

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

        report.setTotalBusinessVisits((visitRepository.countByPage(Page.BUSINESS)));
        report.setLastMonthBusinessVisits(visitRepository.countByPageSince(Page.BUSINESS, oneMonthAgo));
        report.setLastYearBusinessVisits(visitRepository.countByPageSince(Page.BUSINESS, oneYearAgo));

        List<Object[]> popularTypes = visitRepository.findMostPopularTypeBusiness(Page.BUSINESS);
        if (!popularTypes.isEmpty()) {
            Optional<TypeBusiness> typeBusiness = typeBusinessRepository.findById((Long) popularTypes.getFirst()[0]);
            typeBusiness.ifPresent(type -> report.setMostPopularTypeBusiness(type.getName()));
        } else {
            report.setMostPopularTypeBusiness("N/A");
        }

        report.setTotalEventVisits(visitRepository.countByPage(Page.EVENT));
        report.setLastMonthEventVisits(visitRepository.countByPageSince(Page.EVENT, oneMonthAgo));
        report.setLastYearEventVisits(visitRepository.countByPageSince(Page.EVENT, oneYearAgo));

        report.setTotalTouristPlaceVisits(visitRepository.countByPage(Page.TOURIST_PLACE));
        report.setLastMonthTouristPlaceVisits(visitRepository.countByPageSince(Page.TOURIST_PLACE, oneMonthAgo));
        report.setLastYearTouristPlaceVisits(visitRepository.countByPageSince(Page.TOURIST_PLACE, oneYearAgo));

        return report;
    }

    public String generateReportPdf() throws IOException, GeneralSecurityException {
        ReportDTO report = generateReport();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        /*String headerImagePath = "src/main/resources/static/header.png";
        ImageData headerImageData = ImageDataFactory.create(Paths.get(headerImagePath).toUri().toString());*/
        String headerImageUrl = "https://i.ibb.co/whpsnxb/header.png";
        ImageData headerImageData = ImageDataFactory.create(headerImageUrl);
        Image headerImage = new Image(headerImageData);
        headerImage.setWidth(UnitValue.createPercentValue(100));
        document.add(headerImage);

        document.add(new Paragraph("Reporte de Visitas")
                .setFontSize(20)
                .setFontColor(ColorConstants.BLACK)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        document.add(new Paragraph("Fecha de generación: " + currentDate)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(10));

        document.add(new Paragraph("Visitas a Negocios").setFontSize(16).setBold());
        Table businessTable = new Table(2);
        businessTable.setWidth(UnitValue.createPercentValue(100));
        businessTable.addCell("Total de Visitas a Negocios");
        businessTable.addCell(String.valueOf(report.getTotalBusinessVisits()));
        businessTable.addCell("Visitas en el Último Mes");
        businessTable.addCell(String.valueOf(report.getLastMonthBusinessVisits()));
        businessTable.addCell("Visitas en el Último Año");
        businessTable.addCell(String.valueOf(report.getLastYearBusinessVisits()));
        businessTable.addCell("Tipo de Negocio Más Popular");
        businessTable.addCell(report.getMostPopularTypeBusiness());
        document.add(businessTable);

        document.add(new Paragraph("Visitas a Eventos").setFontSize(16).setBold());
        Table eventTable = new Table(2);
        eventTable.setWidth(UnitValue.createPercentValue(100));
        eventTable.addCell("Total de Visitas a Eventos");
        eventTable.addCell(String.valueOf(report.getTotalEventVisits()));
        eventTable.addCell("Visitas en el Último Mes");
        eventTable.addCell(String.valueOf(report.getLastMonthEventVisits()));
        eventTable.addCell("Visitas en el Último Año");
        eventTable.addCell(String.valueOf(report.getLastYearEventVisits()));
        document.add(eventTable);

        document.add(new Paragraph("Visitas a Lugares Turísticos").setFontSize(16).setBold());
        Table touristPlaceTable = new Table(2);
        touristPlaceTable.setWidth(UnitValue.createPercentValue(100));
        touristPlaceTable.addCell("Total de Visitas a Lugares Turísticos");
        touristPlaceTable.addCell(String.valueOf(report.getTotalTouristPlaceVisits()));
        touristPlaceTable.addCell("Visitas en el Último Mes");
        touristPlaceTable.addCell(String.valueOf(report.getLastMonthTouristPlaceVisits()));
        touristPlaceTable.addCell("Visitas en el Último Año");
        touristPlaceTable.addCell(String.valueOf(report.getLastYearTouristPlaceVisits()));
        document.add(touristPlaceTable);

        document.close();

        byte[] pdfBytes = baos.toByteArray();
        MultipartFile pdfFile = generateMultipartFileFromPdf(pdfBytes);

        return fileUploadService.uploadPdf(pdfFile);
    }

    private MultipartFile generateMultipartFileFromPdf(byte[] pdfBytes) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        return new MultipartFile() {
            @Override
            public String getName() {
                return "reporte-visitas.pdf";
            }

            @Override
            public String getOriginalFilename() {
                return "reporte-visitas.pdf";
            }

            @Override
            public String getContentType() {
                return "application/pdf";
            }

            @Override
            public boolean isEmpty() {
                return pdfBytes.length == 0;
            }

            @Override
            public long getSize() {
                return pdfBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return pdfBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(pdfBytes);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
    }
}
