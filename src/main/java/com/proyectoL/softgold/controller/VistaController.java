package com.proyectoL.softgold.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/buscar")
    public String buscar(@RequestParam("query") String query, RedirectAttributes redirectAttrs) {
        // Normalizar el término de búsqueda
        String termino = query.trim().toLowerCase();

        // Redirigir según el término de búsqueda
        switch (termino) {
            case "registrarse":
            case "registro":
                return "redirect:/registro";
            case "iniciar sesion":
            case "inicio sesion":
                return "redirect:/login";
            case "inicio":
            case "Inicio":
                return "redirect:/inicio";
            case "mapas":
            case "Mapas":
                return "redirect:/mapas";
            case "minas":
            case "Minas":
                return "redirect:/minas";
            case "panel admin":
            case "panel de administracion":
                return "redirect:/admin";
            default:
                redirectAttrs.addFlashAttribute("error", "No se encontró la sección solicitada.");
                return "redirect:/";
        }
    }

    private String obtenerRol(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("SIN ROL");
    }

    @GetMapping("/mapas")
    public String vistaMapas() {
        return "redirect:/admin/mapas";
    }

    @GetMapping("/minas")
    public String vistaMinas() {
        return "redirect:/admin/minas";
    }
}
