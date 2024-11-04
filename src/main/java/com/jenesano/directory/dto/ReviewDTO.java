package com.jenesano.directory.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private int review;
    private String description;
    private Long userId;
}
