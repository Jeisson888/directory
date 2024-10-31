package com.jenesano.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "businesses")
@Data
@NoArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String rut;

    private String commercialRegistration;
    private LocalDate registrationDate;

    @NotNull
    private String legalRepresentative;

    private String address;
    private String phoneNumber;
    private String website;
    private String description;

    private boolean validated;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_business_id", nullable = false)
    private TypeBusiness typeBusiness;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id")
    private List<BusinessHour> businessHours;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id")
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id")
    private List<BusinessContent> businessContents;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id")
    private List<Review> reviews;

    public Business(String name, String rut, String commercialRegistration, LocalDate registrationDate,
                    String legalRepresentative, String address, String phoneNumber, String website, String description,
                    boolean validated, Status status, TypeBusiness typeBusiness, User user) {
        this.name = name;
        this.rut = rut;
        this.commercialRegistration = commercialRegistration;
        this.registrationDate = registrationDate;
        this.legalRepresentative = legalRepresentative;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.description = description;
        this.validated = validated;
        this.status = status;
        this.typeBusiness = typeBusiness;
        this.user = user;

        this.businessHours = new ArrayList<>();
        this.images = new ArrayList<>();
        this.businessContents = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }
}
