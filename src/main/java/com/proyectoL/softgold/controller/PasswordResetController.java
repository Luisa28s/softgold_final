package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.service.PasswordResetService;
import com.proyectoL.softgold.service.EmailServiceIface;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Controlador encargado de manejar la recuperación de contraseñas
@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private EmailServiceIface emailService; // Se inyecta el servicio de correo

    // Envía token de recuperacion al correo del usuario
    @PostMapping("/send-reset-token")
    public String sendResetToken(@RequestParam String email) {
        Optional<Usuario> usuarioOpt = passwordResetService.findUserByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return "El correo no está registrado";
        }

        String token = java.util.UUID.randomUUID().toString();
        passwordResetService.createPasswordResetTokenForUser(email, token);

        // Enlace para restablecer (ajusta la URL si tienes frontend)
        String resetLink = "http://localhost:9090/api/password/reset-form?token=" + token;

        // Enviar correo
        emailService.sendPasswordResetEmail(email, resetLink);

        return "Se ha enviado un correo con el enlace para restablecer la contraseña";
    }

    // Verifica si el token es válido
    @GetMapping("/validate-token")
    public String validateToken(@RequestParam String token) {
        boolean valid = passwordResetService.validatePasswordResetToken(token);
        return valid ? "Token válido" : "Token inválido o expirado";
    }

    // Cambia la contraseña usando el token
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!passwordResetService.validatePasswordResetToken(token)) {
            return ResponseEntity.badRequest().body("El token ha expirado o no es válido.");
        }

        if (!newPassword.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            return ResponseEntity.badRequest()
                    .body("La nueva contraseña debe tener al menos una mayúscula y un número.");
        }

        passwordResetService.changePassword(token, newPassword);
        return ResponseEntity.ok("Contraseña actualizada exitosamente.");
    }

}
