package com.jenesano.directory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class ReportDTO {
    private int totalBusinessVisits;
    private int lastMonthBusinessVisits;
    private int lastYearBusinessVisits;
    private String mostPopularTypeBusiness;
    private int totalEventVisits;
    private int lastMonthEventVisits;
    private int lastYearEventVisits;
    private int totalTouristPlaceVisits;
    private int lastMonthTouristPlaceVisits;
    private int lastYearTouristPlaceVisits;
}
