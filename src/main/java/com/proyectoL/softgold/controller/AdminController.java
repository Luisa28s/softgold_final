package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("")
    public String vistaAdmin(Model model, Authentication authentication) {
        // Obtener el usuario autenticado
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
        } else {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/login";
        }

        // Agregar información general para la vista de administrador
        model.addAttribute("rolUsuario", "ADMINISTRADOR");
        return "vistas/admin";
    }
}