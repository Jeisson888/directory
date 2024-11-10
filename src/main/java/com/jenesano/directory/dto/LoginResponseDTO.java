package com.jenesano.directory.dto;

import com.jenesano.directory.entity.TypeUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String jwt;
    private TypeUser typeUser;
}
