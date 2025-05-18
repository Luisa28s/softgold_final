package com.proyectoL.softgold.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyectoL.softgold.model.Mapa;
import com.proyectoL.softgold.repository.MapaDAO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/mapas")
public class MapaController {

    @Autowired
    private MapaDAO mapaDAO;

    @GetMapping("")
    public String listarMapas(Model model) {
        model.addAttribute("mapas", mapaDAO.findAll());
        return "vistas/listarMapas";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrearMapa(Model model) {
        model.addAttribute("mapa", new Mapa());
        return "vistas/crearMapa";
    }

    @PostMapping("/crear")
    public String crearMapa(@Valid @ModelAttribute("mapa") Mapa mapa, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "vistas/crearMapa";
        }
        mapaDAO.save(mapa);
        return "redirect:/admin/mapas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMapa(@PathVariable Long id, Model model) {
        Mapa mapa = mapaDAO.findById(id).orElse(null);
        if (mapa == null) {
            return "redirect:/admin/mapas";
        }
        model.addAttribute("mapa", mapa);
        return "vistas/editarMapa";
    }

    @PostMapping("/editar/{id}")
    public String editarMapa(@PathVariable Long id, @Valid @ModelAttribute("mapa") Mapa mapa, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "vistas/editarMapa";
        }
        mapa.setCodigoMapa(id); // Asegura que el ID no se pierda
        mapaDAO.save(mapa);
        return "redirect:/admin/mapas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMapa(@PathVariable Long id) {
        mapaDAO.deleteById(id);
        return "redirect:/admin/mapas";
    }

}
