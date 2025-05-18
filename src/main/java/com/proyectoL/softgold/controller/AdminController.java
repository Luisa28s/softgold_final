package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String vistaAdmin(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
        } else {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/login";
        }

        model.addAttribute("rolUsuario", "ADMINISTRADOR");
        return "vistas/admin";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
        } else {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin";
        }
        return "vistas/perfilAdmin";
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model,
            Authentication authentication) {
        if (result.hasErrors()) {
            return "vistas/perfilAdmin";
        }
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuarioActual = usuarioOpt.get();
            usuarioActual.setEmail(usuario.getEmail());

            if (usuario.getPasswordPlano() != null && !usuario.getPasswordPlano().isBlank()) {
                String passwordEncriptada = passwordEncoder.encode(usuario.getPasswordPlano());
                usuarioActual.setPassword(passwordEncriptada);
            }

            usuarioDAO.save(usuarioActual);
            model.addAttribute("exito", "Perfil actualizado correctamente.");
        } else {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin";
        }
        return "redirect:/admin/perfil";
    }
}