package com.jenesano.directory.dto;

import com.jenesano.directory.entity.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BusinessDTO {
    private String name;
    private String rut;
    private String commercialRegistration;
    private LocalDate registrationDate;
    private String legalRepresentative;
    private String address;
    private String phoneNumber;
    private String website;
    private String description;
    private Status status;
    private Long typeBusinessId;
    private String email;
}
