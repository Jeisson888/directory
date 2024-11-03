package com.jenesano.directory.service;

import com.jenesano.directory.dto.EmailDTO;
import com.jenesano.directory.dto.LoginDTO;
import com.jenesano.directory.dto.UserDTO;
import com.jenesano.directory.entity.Status;
import com.jenesano.directory.entity.TypeUser;
import com.jenesano.directory.entity.User;
import com.jenesano.directory.exception.EntityNotFoundException;
import com.jenesano.directory.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtService jwtService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario", userId));
    }

    public User createAdminUser(UserDTO userDTO) {
        // validar
        User user = new User(
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getEmail(),
                Status.ENABLED,
                TypeUser.ADMIN
        );

        return userRepository.save(user);
    }

    public User createManagerUser(UserDTO userDTO) {
        // validar
        User user = new User(
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getEmail(),
                Status.ENABLED,
                TypeUser.MANAGER
        );

        return userRepository.save(user);
    }

    public User createOwnerUser(String email) {
        // validar
        String username = email.split("@")[0];

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        String randomPassword = RandomStringUtils.randomAlphanumeric(5);

        User user = new User(
                username,
                passwordEncoder.encode(randomPassword),
                email,
                Status.DISABLED,
                TypeUser.OWNER
        );

        return userRepository.save(user);
    }

    public User createTouristUser(EmailDTO emailDTO) {
        // validar
        String username = emailDTO.getEmail().split("@")[0];
        String randomPassword = RandomStringUtils.randomAlphanumeric(5);

        User user = new User(
                username,
                passwordEncoder.encode(randomPassword),
                emailDTO.getEmail(),
                Status.ENABLED,
                TypeUser.TOURIST
        );

        userRepository.save(user);
        emailService.sendCredentialsToEmail(emailDTO.getEmail(), username, randomPassword);
        return user;
    }

    public User updatePassword(Long userId) {
        User user = getUserById(userId);

        return null;
    }

    public User updateStatus(Long userId) {
        User user = getUserById(userId);

        return null;
    }

    public User resetPassword(Long userId) {
        User user = getUserById(userId);

        return null;
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw  new EntityNotFoundException("Usuario", userId);
        }
        userRepository.deleteById(userId);
    }

    public String login(LoginDTO loginDTO) {
        // validar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
        return jwtService.generateToken(userDetails);
    }

    public String refreshToken() {
        return null;
    }

    public void logout() {

    }
}
