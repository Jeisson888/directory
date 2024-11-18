package com.jenesano.directory.service;

import com.jenesano.directory.dto.*;
import com.jenesano.directory.entity.Status;
import com.jenesano.directory.entity.TypeUser;
import com.jenesano.directory.entity.User;
import com.jenesano.directory.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateAdminUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("adminUser");
        userDTO.setPassword("password123");
        userDTO.setEmail("admin@example.com");

        User savedUser = new User("adminUser", "encodedPassword", "admin@example.com", Status.ENABLED, TypeUser.ADMIN);

        Mockito.when(userRepository.existsByUsername("adminUser")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        User result = userService.createAdminUser(userDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("adminUser", result.getUsername());
        Assertions.assertEquals(TypeUser.ADMIN, result.getTypeUser());
    }

    @Test
    void testCreateManagerUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("managerUser");
        userDTO.setPassword("password123");
        userDTO.setEmail("manager@example.com");

        User savedUser = new User("managerUser", "encodedPassword", "manager@example.com", Status.ENABLED, TypeUser.MANAGER);

        Mockito.when(userRepository.existsByUsername("managerUser")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("manager@example.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        User result = userService.createManagerUser(userDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("managerUser", result.getUsername());
        Assertions.assertEquals(TypeUser.MANAGER, result.getTypeUser());
    }

    @Test
    void testCreateOwnerUser() {
        String email = "owner@example.com";
        String encodedPassword = "encodedPassword";
        User savedUser = new User("owner", encodedPassword, email, Status.DISABLED, TypeUser.OWNER);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        User result = userService.createOwnerUser(email);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(email, result.getEmail());
        Assertions.assertEquals(Status.DISABLED, result.getStatus());
        Assertions.assertEquals(TypeUser.OWNER, result.getTypeUser());
    }

    @Test
    void testUpdatePassword() {
        Long userId = 1L;
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setOldPassword("oldPassword");
        updatePasswordDTO.setNewPassword("newPassword123");

        User user = new User("user1", "encodedOldPassword", "user@example.com", Status.ENABLED, TypeUser.TOURIST);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        Mockito.when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User result = userService.updatePassword(userId, updatePasswordDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("encodedNewPassword", result.getEncryptedPassword());
    }

    @Test
    void testLogin() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("user1");
        loginDTO.setPassword("password123");

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        User user = new User("user1", "encodedPassword", "user@example.com", Status.ENABLED, TypeUser.TOURIST);
        String generatedToken = "jwtToken";

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);
        Mockito.when(userDetailsService.loadUserByUsername("user1")).thenReturn(userDetails);
        Mockito.when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateToken(userDetails)).thenReturn(generatedToken);

        LoginResponseDTO response = userService.login(loginDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(generatedToken, response.getJwt());
        Assertions.assertEquals(TypeUser.TOURIST, response.getTypeUser());
    }
}
