package com.jenesano.directory.dto;

import com.jenesano.directory.entity.TypeUser;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
}
