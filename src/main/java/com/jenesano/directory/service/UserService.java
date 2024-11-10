package com.jenesano.directory.service;

import com.jenesano.directory.dto.EmailDTO;
import com.jenesano.directory.dto.LoginDTO;
import com.jenesano.directory.dto.LoginResponseDTO;
import com.jenesano.directory.dto.UserDTO;
import com.jenesano.directory.entity.Status;
import com.jenesano.directory.entity.TypeUser;
import com.jenesano.directory.entity.User;
import com.jenesano.directory.exception.EmailAlreadyExistsException;
import com.jenesano.directory.exception.EntityNotFoundException;
import com.jenesano.directory.exception.UsernameAlreadyExistsException;
import com.jenesano.directory.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        validateUserDTO(userDTO);
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException(userDTO.getUsername());
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }
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
        validateUserDTO(userDTO);
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException(userDTO.getUsername());
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }
        User user = new User(
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getEmail(),
                Status.ENABLED,
                TypeUser.MANAGER
        );

        return userRepository.save(user);
    }

    private void validateUserDTO(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty() || userDTO.getPassword() == null
                || userDTO.getPassword().isEmpty() || userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario, contraseña y correo del usuario no pueden ser nulos o vacios.");
        }
    }

    public User createOwnerUser(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario no puede ser nulo o vacio.");
        }
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            if (existingUser.get().getTypeUser() == TypeUser.OWNER) {
                return existingUser.get();
            } else {
                throw new EmailAlreadyExistsException(email);
            }
        }
        String username = email.split("@")[0];
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
        if (emailDTO.getEmail() == null || emailDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario no puede ser nulo o vacio.");
        }
        if (userRepository.existsByEmail(emailDTO.getEmail())) {
            throw new EmailAlreadyExistsException(emailDTO.getEmail());
        }
        String username = emailDTO.getEmail().split("@")[0];
        String randomPassword = RandomStringUtils.randomAlphanumeric(5);
        User user = new User(
                username,
                passwordEncoder.encode(randomPassword),
                emailDTO.getEmail(),
                Status.ENABLED,
                TypeUser.TOURIST
        );

        emailService.sendCredentialsToEmail(emailDTO.getEmail(), username, randomPassword);
        userRepository.save(user);
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
            throw new EntityNotFoundException("Usuario", userId);
        }
        userRepository.deleteById(userId);
    }

    public LoginResponseDTO login(LoginDTO loginDTO) {
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()
                || loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario y contraseña no pueden ser nulos o vacios.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
        String jwt = jwtService.generateToken(userDetails);
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con nombre de usuario " + loginDTO.getUsername() + " no encontrado."));
        return new LoginResponseDTO(user.getId(), jwt, user.getTypeUser());
    }

    public String refreshToken() {
        return null;
    }

    public void logout() {

    }
}
