package com.proyectoL.softgold.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//Maneja el envio de correos para recuperar contraseñas
@Service
public class EmailService implements EmailServiceIface {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Restablecer contraseña - SoftGold");
        message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña:\n" + resetLink);
        mailSender.send(message);
    }
}