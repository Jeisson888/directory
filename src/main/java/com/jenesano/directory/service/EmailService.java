package com.jenesano.directory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCredentialsToEmail(String email, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Credenciales de acceso");
        message.setText("Hola,\n\nTus credenciales de acceso son:\n\n" +
                "Usuario: " + username + "\n" +
                "Contraseña: " + password + "\n\n" +
                "Por favor, cambia tu contraseña tras el primer inicio de sesión.");

        mailSender.send(message);
    }
}
