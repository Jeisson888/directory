package com.jenesano.directory.controller;

import com.jenesano.directory.dto.*;
import com.jenesano.directory.entity.User;
import com.jenesano.directory.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Obtener todos los usuarios",
            description = "Obtiene todos los usuarios en el sistema. Solo accesible para el rol ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Obtener un usuario por ID",
            description = "Obtiene un usuario específico mediante su ID. Solo accesible para el rol ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Crear un usuario con rol ADMIN",
            description = "Permite crear un nuevo usuario con rol ADMIN.")
    @PostMapping("/admin")
    public ResponseEntity<User> createAdminUser(@RequestBody UserDTO userDTO) {
        User user = userService.createAdminUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Crear un usuario con rol MANAGER",
            description = "Permite crear un nuevo usuario con rol MANAGER. Solo accesible para los roles ADMIN y MANAGER.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/manager")
    public ResponseEntity<User> createManagerUser(@RequestBody UserDTO userDTO) {
        User user = userService.createManagerUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Crear un usuario con rol TOURIST",
            description = "Permite crear un nuevo usuario con rol TOURIST mediante el correo electrónico.")
    @PostMapping("/tourist")
    public ResponseEntity<User> createTouristUser(@RequestBody EmailDTO emailDTO) {
        User user = userService.createTouristUser(emailDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar la contraseña de un usuario",
            description = "Permite a los usuarios con roles ADMIN, MANAGER, OWNER o TOURIST actualizar su contraseña.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OWNER', 'TOURIST')")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updatePassword(@PathVariable Long userId, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User user = userService.updatePassword(userId, updatePasswordDTO);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Restablecer la contraseña de un usuario",
            description = "Permite restablecer la contraseña de un usuario mediante su correo electrónico.")
    @PutMapping("/reset-password")
    public ResponseEntity<User> resetPassword(@RequestBody EmailDTO emailDTO) {
        User user = userService.resetPassword(emailDTO);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Eliminar un usuario",
            description = "Permite eliminar un usuario específico mediante su ID. Solo accesible para el rol ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Iniciar sesión",
            description = "Permite a un usuario iniciar sesión proporcionando las credenciales de login.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        LoginResponseDTO loginResponseDTO = userService.login(loginDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @Operation(summary = "Refrescar el token de sesión",
            description = "Permite refrescar el token de sesión para mantener la autenticación del usuario.")
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cerrar sesión",
            description = "Permite cerrar la sesión del usuario.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.noContent().build();
    }
}
