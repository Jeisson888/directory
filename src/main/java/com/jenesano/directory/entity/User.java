package com.jenesano.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String encryptedPassword;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeUser typeUser;

    public User(String username, String encryptedPassword, String email, Status status, TypeUser typeUser) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.email = email;
        this.status = status;
        this.typeUser = typeUser;
    }
}
