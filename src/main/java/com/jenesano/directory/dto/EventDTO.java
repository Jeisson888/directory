package com.jenesano.directory.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDTO {
    private String name;
    private String description;
    private LocalDate date;
}
