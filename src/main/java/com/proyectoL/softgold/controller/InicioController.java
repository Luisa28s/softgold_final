package com.proyectoL.softgold.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping({ "/", "/inicio" })
    public String inicio(Model model) {
        model.addAttribute("titulo", "Bienvenido a SoftGold");
        return "vistas/inicio";
    }
}
