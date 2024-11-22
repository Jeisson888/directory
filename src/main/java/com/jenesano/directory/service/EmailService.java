package com.jenesano.directory.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Método para enviar un correo electrónico con las credenciales de acceso del usuario.
    public void sendCredentialsToEmail(String email, String username, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; color: #333;'>"
                + "<table style='width: 100%; border: none;'>"
                + "<tr><td style='text-align: center; padding: 20px; background-color: #1A5414; color: #ffffff;'>"
                + "<h1 style='margin: 0;'>Bienvenido al Directorio de Jenesano</h1>"
                + "<p style='margin: 5px 0;'>Descubre los mejores negocios, eventos y sitios turísticos de nuestro municipio</p>"
                + "</td></tr>"
                + "<tr><td style='padding: 20px; background-color: #f4f4f4;'>"
                + "<p>Aquí están tus credenciales de acceso:</p>"
                + "<table style='width: 100%; margin-top: 20px; margin-bottom: 20px;'>"
                + "<tr><td style='padding: 10px; border-bottom: 1px solid #ddd;'><strong>Usuario:</strong></td>"
                + "<td style='padding: 10px; border-bottom: 1px solid #ddd;'>" + username + "</td></tr>"
                + "<tr><td style='padding: 10px; border-bottom: 1px solid #ddd;'><strong>Contraseña:</strong></td>"
                + "<td style='padding: 10px; border-bottom: 1px solid #ddd;'>" + password + "</td></tr>"
                + "</table>"
                + "<p>Por seguridad, te recomendamos cambiar tu contraseña.</p>"
                + "</td></tr>"
                + "<tr><td style='text-align: center; padding: 10px; background-color: #1A5414; color: #ffffff;'>"
                + "<p style='margin: 0;'>¡Gracias por ser parte del Directorio de Jenesano!</p>"
                + "</td></tr>"
                + "</table></div>";

        helper.setTo(email);
        helper.setSubject("Credenciales de acceso");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    // Método para enviar un correo electrónico con las credenciales de acceso después de una solicitud de recuperación de contraseña.
    public void sendPasswordRecoveryToEmail(String email, String username, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; color: #333;'>"
                + "<table style='width: 100%; border: none;'>"
                + "<tr><td style='text-align: center; padding: 20px; background-color: #1A5414; color: #ffffff;'>"
                + "<h1 style='margin: 0;'>Recuperación de Contraseña</h1>"
                + "<p style='margin: 5px 0;'>Directorio de Jenesano</p>"
                + "</td></tr>"
                + "<tr><td style='padding: 20px; background-color: #f4f4f4;'>"
                + "<p>Hola, " + username + ",</p>"
                + "<p>Has solicitado recuperar tu contraseña. Aquí están tus credenciales de acceso:</p>"
                + "<table style='width: 100%; margin-top: 20px; margin-bottom: 20px;'>"
                + "<tr><td style='padding: 10px; border-bottom: 1px solid #ddd;'><strong>Usuario:</strong></td>"
                + "<td style='padding: 10px; border-bottom: 1px solid #ddd;'>" + username + "</td></tr>"
                + "<tr><td style='padding: 10px; border-bottom: 1px solid #ddd;'><strong>Contraseña:</strong></td>"
                + "<td style='padding: 10px; border-bottom: 1px solid #ddd;'>" + password + "</td></tr>"
                + "</table>"
                + "<p>Por seguridad, te recomendamos cambiar tu contraseña.</p>"
                + "</td></tr>"
                + "<tr><td style='text-align: center; padding: 10px; background-color: #1A5414; color: #ffffff;'>"
                + "<p style='margin: 0;'>¡Estamos para ayudarte!</p>"
                + "</td></tr>"
                + "</table></div>";

        helper.setTo(email);
        helper.setSubject("Recuperación de contraseña");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
