package com.jenesano.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Page page;

    private Long typeBusinessId;

    @NotNull
    private LocalDateTime timestamp;

    public Visit(Page page, Long typeBusinessId, LocalDateTime timestamp) {
        this.page = page;
        this.typeBusinessId = typeBusinessId;
        this.timestamp = timestamp;
    }

    public Visit(Page page, LocalDateTime timestamp) {
        this.page = page;
        this.timestamp = timestamp;
    }
}
