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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con nombre de usuario " + username + " no encontrado."));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getEncryptedPassword(),
                user.getStatus() == Status.ENABLED,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getTypeUser().name()))
        );
    }
}
