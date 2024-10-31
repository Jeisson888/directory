package com.jenesano.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "business_hours")
@Data
@NoArgsConstructor
public class BusinessHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DayWeek dayWeek;

    @NotNull
    private LocalTime openingTime;

    @NotNull
    private LocalTime closingTime;

    public BusinessHour(DayWeek dayWeek, LocalTime openingTime, LocalTime closingTime) {
        this.dayWeek = dayWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
}
