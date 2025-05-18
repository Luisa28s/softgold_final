package com.proyectoL.softgold.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Controlador encargado de redirigir a los usuarios a la pagina según su rol
@Controller
public class RedireccionController {

    @GetMapping("/redirectByRole")
    public String redirectByRole(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "redirect:/admin";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MINERO"))) {
            return "redirect:/minero/inicio";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLEADO"))) {
            return "redirect:/empleado/inicio";

        } else {
            return "redirect:/";

        }

    }

}
