package com.proyectoL.softgold.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.PasswordResetTokenDAO;
import com.proyectoL.softgold.repository.RolDAO;
import com.proyectoL.softgold.repository.UsuarioDAO;

import jakarta.validation.Valid;

// Controlador para que el superadmin gestion a los administradores 
@Controller
@RequestMapping("/super-admin")
public class AdminController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private RolDAO rolDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;

    @GetMapping(" ")
    public String vistaSuperadmin(Model model, Authentication authentication) {
        model.addAttribute("nombreUsuario", authentication.getName());
        model.addAttribute("rolUsuario", obtenerRol(authentication));

        // Obtener lista de administradores
        Rol rolAdmin = rolDAO.findByNombre("ADMINISTRADOR");
        Rol rolSuperAdmin = rolDAO.findByNombre("SUPERADMIN");
        List<Usuario> administradores = usuarioDAO.findByRolesIn(List.of(rolAdmin, rolSuperAdmin));

        model.addAttribute("listaAdmins", administradores);
        return "vistas/superAdmin";
    }

    @GetMapping("/crear-admin")
    public String mostrarFormularioCrearAdmin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "vistas/crearAdmin";
    }

    @PostMapping("/crear-admin")
    public String procesarCrearAdmin(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "vistas/crearAdmin";
        }

        if (usuarioDAO.findByEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("error", "Ya existe un usuario con ese correo.");
            return "vistas/crearAdmin";
        }

        // Asignar valores predeterminados si están vacíos
        if (usuario.getNombre1() == null || usuario.getNombre1().isBlank())
            usuario.setNombre1("Admin");
        if (usuario.getNombre2() == null)
            usuario.setNombre2("");
        if (usuario.getApellido1() == null || usuario.getApellido1().isBlank())
            usuario.setApellido1("Sistema");
        if (usuario.getApellido2() == null || usuario.getApellido2().isBlank())
            usuario.setApellido2("Softgold");

        // Codificar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlano()));

        // Asignar el rol "ADMINISTRADOR" al nuevo usuario
        Rol rolAdmin = rolDAO.findByNombre("ADMINISTRADOR");
        if (rolAdmin == null) {
            model.addAttribute("error", "El rol ADMINISTRADOR no existe en la base de datos.");
            return "vistas/crearAdmin";
        }
        usuario.setRoles(Collections.singleton(rolAdmin));

        // Guardar el usuario en la base de datos
        usuarioDAO.save(usuario);

        redirectAttrs.addFlashAttribute("mensaje", "Administrador creado correctamente.");
        return "redirect:/super-admin";
    }

    private String obtenerRol(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse("SIN ROL");
    }

    @Transactional
    @GetMapping("/eliminar-admin/{id}")
    public String eliminarAdmin(@org.springframework.web.bind.annotation.PathVariable Long id,
            RedirectAttributes redirectAttrs,
            Authentication authentication) {

        // Evita que el superadmin se elimine a sí mismo
        Usuario actual = usuarioDAO.findByEmail(authentication.getName()).orElse(null);
        if (actual != null && actual.getId().equals(id)) {
            redirectAttrs.addFlashAttribute("error", "No puedes eliminar tu propio usuario.");
            return "redirect:/super-admin";
        }

        // Buscar el usuario y eliminar si existe
        Usuario admin = usuarioDAO.findById(id).orElse(null);
        if (admin != null) {
            // Eliminar registros relacionados en password_reset_token
            passwordResetTokenDAO.deleteByUsuarioId(id);

            // Eliminar el usuario
            usuarioDAO.delete(admin);
            redirectAttrs.addFlashAttribute("mensaje", "Administrador eliminado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Administrador no encontrado.");
        }

        return "redirect:/super-admin";
    }

}
