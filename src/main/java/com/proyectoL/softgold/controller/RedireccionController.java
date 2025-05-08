package com.proyectoL.softgold.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Controlador encargado de redirigir a los usuarios a la pagina según su rol
@Controller
public class RedireccionController {

    @GetMapping("/redirectByRole")
    public String redirectByRole(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "redirect:/admin";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO"))) {
            return "redirect:/usuario";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_INVITADO"))) {
            return "redirect:/invitado";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
            return "redirect:/super-admin";
        }

        return "redirect:/login?error";
    }

}
