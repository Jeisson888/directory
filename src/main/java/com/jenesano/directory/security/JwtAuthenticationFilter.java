package com.jenesano.directory.security;

import com.jenesano.directory.service.JwtService;
import com.jenesano.directory.service.UserDetailsServiceImpl;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Servicio que gestiona la generación y validación de JWT
    private final JwtService jwtService;

    // Servicio que carga los detalles del usuario (como roles y permisos)
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    // Filtro que se ejecuta en cada solicitud para verificar la validez del token JWT.
    // Si el token es válido, se autentica al usuario en el contexto de seguridad de Spring.
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        // Obtiene el encabezado "Authorization" de la solicitud, donde se espera que venga el token JWT
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        // Valida que el encabezado no sea nulo y que el token comience con el prefijo "Bearer"
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extrae el token eliminando el prefijo "Bearer "
            username = jwtService.extractUsername(jwt); // Extrae el nombre de usuario del token
        }

        // Verifica si el usuario obtenido del token no está autenticado actualmente en el contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carga los detalles del usuario desde el servicio
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            // Valida que el token corresponda al usuario cargado
            if (jwtService.validateToken(jwt, userDetails)) {
                // Crea un token de autenticación para Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // Añade los detalles de la solicitud actual al token de autenticación
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Establece la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continúa con el siguiente filtro en la cadena de seguridad
        filterChain.doFilter(request, response);
    }
}
