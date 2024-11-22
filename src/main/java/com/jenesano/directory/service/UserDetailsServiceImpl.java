package com.jenesano.directory.service;

import com.jenesano.directory.entity.Status;
import com.jenesano.directory.entity.User;
import com.jenesano.directory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Carga un usuario por su nombre de usuario, y si no se encuentra, lanza una excepción de "Usuario no encontrado".
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario en el repositorio por su nombre de usuario.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con nombre de usuario " + username + " no encontrado."));

        // Devuelve un objeto UserDetails que contiene los detalles del usuario para autenticación.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),  // Nombre de usuario
                user.getEncryptedPassword(),  // Contraseña encriptada
                user.getStatus() == Status.ENABLED,  // Si el usuario está habilitado
                true,  // La cuenta no está vencida
                true,  // Las credenciales no están vencidas
                true,  // La cuenta no está bloqueada
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getTypeUser().name()))  // Rol del usuario basado en su tipo
        );
    }
}
