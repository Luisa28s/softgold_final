package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.PasswordResetToken;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.service.PasswordResetServiceIface;
import com.proyectoL.softgold.service.UsuarioServiceIface;
import com.proyectoL.softgold.service.EmailServiceIface;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//Controlador encargado de manejar la recuperacion de contraseñas desde una interfaz web
@Controller
public class PasswordController {

    private final UsuarioServiceIface usuarioService;
    private final PasswordResetServiceIface passwordResetService;
    private final EmailServiceIface emailService;

    // Constructor con inyección de dependencias
    public PasswordController(UsuarioServiceIface usuarioService,
            PasswordResetServiceIface passwordResetService,
            EmailServiceIface emailService) {
        this.usuarioService = usuarioService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
    }

    // Mostrar formulario para solicitar recuperación de contraseña (correo)
    @GetMapping("/recuperar-contrasena")
    public String showRecoverPasswordForm(Model model) {
        model.addAttribute("titulo", "Recuperar Contraseña");
        return "vistas/recuperarPassword"; // Vista para solicitar correo
    }

    // Procesar la solicitud de recuperación de contraseña
    @PostMapping("/enviar-token")
    public String sendPasswordResetToken(@RequestParam("email") String email, RedirectAttributes flash) {
        Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);

        if (usuario == null) {
            flash.addFlashAttribute("error", "No se encontró un usuario con ese correo");
            return "redirect:/recuperarPassword";
        }

        // Crear el token de recuperación
        PasswordResetToken token = passwordResetService.createPasswordResetToken(usuario);
        String resetLink = "http://localhost:9090/cambiarPassword/" + token.getToken();

        // Enviar correo
        emailService.sendPasswordResetEmail(usuario.getEmail(), resetLink);

        flash.addFlashAttribute("exito", "Te hemos enviado un enlace para cambiar la contraseña");
        return "redirect:/login";
    }

    // Mostrar formulario para cambiar la contraseña
    @GetMapping("/cambiar-contrasena/{token}")
    public String showChangePasswordForm(@PathVariable("token") String token, Model model, RedirectAttributes flash) {
        PasswordResetToken passwordResetToken = passwordResetService.findByToken(token);

        if (passwordResetToken == null) {
            flash.addFlashAttribute("error", "El token de recuperación es inválido o ha expirado");
            return "redirect:/login";
        }

        model.addAttribute("titulo", "Cambiar Contraseña");
        model.addAttribute("token", token);
        return "vistas/cambiarPassword"; // Vista para cambiar la contraseña
    }

    // Procesar el cambio de contraseña
    @PostMapping("/cambiar-contrasena")
    public String processChangePassword(@RequestParam("password") String password,
            @RequestParam("token") String token, RedirectAttributes flash) {
        PasswordResetToken passwordResetToken = passwordResetService.findByToken(token);

        if (passwordResetToken == null) {
            flash.addFlashAttribute("error", "El token de recuperación es inválido o ha expirado");
            return "redirect:/login";
        }

        Usuario usuario = passwordResetToken.getUsuario();
        usuario.setPassword(password); // Aquí deberías aplicar el encriptado a la nueva contraseña
        usuarioService.guardarUsuario(usuario);

        // Invalidar el token después de usarlo
        passwordResetService.delete(passwordResetToken);

        flash.addFlashAttribute("exito", "Tu contraseña ha sido cambiada exitosamente");
        return "redirect:/login";
    }
}
