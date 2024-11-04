package com.jenesano.directory.controller;

import com.jenesano.directory.dto.EmailDTO;
import com.jenesano.directory.dto.LoginDTO;
import com.jenesano.directory.dto.UserDTO;
import com.jenesano.directory.entity.User;
import com.jenesano.directory.service.UserService;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/admin")
    public ResponseEntity<User> createAdminUser(@RequestBody UserDTO userDTO) {
        User user = userService.createAdminUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/manager")
    public ResponseEntity<User> createManagerUser(@RequestBody UserDTO userDTO) {
        User user = userService.createManagerUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/tourist")
    public ResponseEntity<User> createTouristUser(@RequestBody EmailDTO emailDTO) {
        User user = userService.createTouristUser(emailDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String jwt = userService.login(loginDTO);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.noContent().build();
    }
}