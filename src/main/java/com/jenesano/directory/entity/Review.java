package com.jenesano.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int review;

    private String description;

    @NotNull
    private LocalDate creationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeReview typeReview;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Review(int review, String description, LocalDate creationDate, TypeReview typeReview, User user) {
        this.review = review;
        this.description = description;
        this.creationDate = creationDate;
        this.typeReview = typeReview;
        this.user = user;
    }
}
