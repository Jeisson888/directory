package com.jenesano.directory.service;

import com.jenesano.directory.dto.ReportDTO;
import com.jenesano.directory.entity.Page;
import com.jenesano.directory.entity.TypeBusiness;
import com.jenesano.directory.entity.Visit;
import com.jenesano.directory.repository.TypeBusinessRepository;
import com.jenesano.directory.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final VisitRepository visitRepository;
    private final TypeBusinessRepository typeBusinessRepository;

    @Autowired
    public ReportService(VisitRepository visitRepository, TypeBusinessRepository typeBusinessRepository) {
        this.visitRepository = visitRepository;
        this.typeBusinessRepository = typeBusinessRepository;
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
}
