package com.jenesano.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "business_contents")
@Data
@NoArgsConstructor
public class BusinessContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;
    private Integer price;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    public BusinessContent(String name, String description, int price, Image image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }
}
