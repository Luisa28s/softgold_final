package com.proyectoL.softgold.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.UsuarioDAO;

import org.springframework.security.core.Authentication;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/empleado")
public class EmpleadosInicioController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/inicio")
    public String inicioEmpleado(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("nombreUsuario", usuario.getNombre1() + " " + usuario.getApellido1());
        } else {
            model.addAttribute("nombreUsuario", email); // fallback
        }
        model.addAttribute("titulo", "Panel de Empleado");
        return "vistas/inicioEmpleado";
    }

}
