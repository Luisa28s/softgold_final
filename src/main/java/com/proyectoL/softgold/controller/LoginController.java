package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.repository.MinaDAO;
import com.proyectoL.softgold.repository.RolDAO;
import com.proyectoL.softgold.repository.UsuarioDAO;
import com.proyectoL.softgold.service.PasswordResetService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.model.Usuario;

//Controlador que maneja el inicio de sesion, registro de usuarios y recuperacion de contraseñas
@Controller
public class LoginController {

    @Autowired
    private PasswordResetService passwordResetService;

    private final UsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolDAO rolDAO;

    @Autowired
    private MinaDAO minaDAO;

    LoginController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registroExitoso", required = false) String registroExitoso,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales incorrectas. Inténtalo de nuevo.");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión correctamente.");
        }
        if (registroExitoso != null) {
            model.addAttribute("mensaje", "¡Registro exitoso! Ahora puedes iniciar sesión.");
        }
        return "vistas/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        return "redirect:/login?logout";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("minas", minaDAO.findAll()); // <-- Aquí agregas las minas
        return "vistas/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("minas", minaDAO.findAll());
            return "vistas/registro";
        }

        try {
            Optional<Usuario> existente = usuarioDAO.findByEmail(usuario.getEmail());
            if (existente.isPresent()) {
                model.addAttribute("error", "Ya existe una cuenta con ese correo.");
                return "vistas/registro";
            }

            // Asignar rol genérico "USUARIO"
            // Asignar el rol según el tipo de usuario seleccionado en el formulario
            String tipoUsuario = usuario.getTipoUsuario(); // Debe ser "MINERO", "EMPLEADO", etc.
            Rol rol = rolDAO.findByNombre(tipoUsuario);
            if (rol == null) {
                model.addAttribute("error", "El rol '" + tipoUsuario + "' no existe en la base de datos.");
                model.addAttribute("minas", minaDAO.findAll());
                return "vistas/registro";
            }
            usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlano()));
            usuario.setRoles(Collections.singleton(rol));
            usuario.setBloqueado(false);
            usuario.setIntentosFallidos(0);

            usuarioDAO.save(usuario);

            return "redirect:/login?registroExitoso";

        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado: " + e.getMessage());
            return "vistas/registro";
        }
    }

    @GetMapping("/recuperar")
    public String mostrarFormularioRecuperar(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "vistas/recuperarPassword";
    }

    @PostMapping("/recuperar")
    public String enviarCorreoRecuperacion(@RequestParam("email") String email, Model model) {
        try {
            Optional<Usuario> usuarioOpt = passwordResetService.findUserByEmail(email);
            if (usuarioOpt.isPresent()) {
                String token = UUID.randomUUID().toString();
                passwordResetService.createPasswordResetTokenForUser(email, token);

                model.addAttribute("mensaje", "Se ha enviado un enlace a tu correo.");
            } else {
                model.addAttribute("error", "No se encontró una cuenta con ese correo.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al procesar la solicitud.");
        }

        return "vistas/recuperarPassword";
    }

    @GetMapping("/cambiarPassword")
    public String mostrarFormularioCambiarPassword(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "vistas/cambiarPassword";
    }

    @PostMapping("/cambiarPassword")
    public String procesarCambioPassword(@RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmar") String confirmar,
            Model model) {
        if (!password.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            model.addAttribute("token", token);
            return "vistas/cambiarPassword";
        }

        if (!passwordResetService.validatePasswordResetToken(token)) {
            model.addAttribute("error", "El token es inválido o ha expirado.");
            return "vistas/recuperarPassword";
        }

        passwordResetService.changePassword(token, password);
        model.addAttribute("mensaje", "Tu contraseña ha sido restablecida correctamente.");
        return "vistas/login";
    }

    @GetMapping("/cuenta-bloqueada")
    public String cuentaBloqueada() {
        return "vistas/cuentaBloqueada";
    }
}
