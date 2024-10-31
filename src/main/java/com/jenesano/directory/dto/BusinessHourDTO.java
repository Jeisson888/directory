package com.jenesano.directory.dto;

import com.jenesano.directory.entity.DayWeek;
import lombok.Data;

import java.time.LocalTime;

@Data
public class BusinessHourDTO {
    private DayWeek dayWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
