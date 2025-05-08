package com.proyectoL.softgold.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//Controlador encargado de manejar las vistas principales para los diferentes roles 
@Controller
public class VistaController {

    @GetMapping("/admin/panel")
    public String mostrarPanelAdmin(Model model, Authentication authentication) {
        model.addAttribute("nombreUsuario", authentication.getName());
        model.addAttribute("rolUsuario", obtenerRol(authentication));
        return "vistas/admin";
    }

    @GetMapping("/usuario/home")
    public String mostrarPanelUsuario(Model model, Authentication authentication) {
        model.addAttribute("nombreUsuario", authentication.getName());
        model.addAttribute("rolUsuario", obtenerRol(authentication));
        return "vistas/usuario";
    }

    private String obtenerRol(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("SIN ROL");
    }
}
