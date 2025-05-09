package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.service.UsuarioServiceIface;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

//Controlador encargado de gestionar el CRUD de los usuarios
@Controller
@SessionAttributes("usuario")
public class UsuarioController {

    private final UsuarioServiceIface usuarioService;

    public UsuarioController(UsuarioServiceIface usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        model.addAttribute("titulo", "Lista de usuarios");
        model.addAttribute("usuarios", usuarios);
        return "vistas/usuarioListar";
    }

    @GetMapping("/usuario")
    public String vistaUsuario(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName()).orElse(null);
        model.addAttribute("usuario", usuario);
        return "vistas/usuario";
    }

    @GetMapping("/usuario/admin")
    public String vistaAdmin(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName()).orElse(null);
        model.addAttribute("usuario", usuario);
        return "vistas/admin";
    }

    @GetMapping("/formulario")
    public String formpost(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("titulo", "Nuevo usuario");
        model.addAttribute("usuario", usuario);
        return "vistas/usuarioForm";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model,
            SessionStatus status, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de usuario");
            model.addAttribute("advertencia", "Corrige los errores del formulario");
            return "vistas/usuarioForm";
        }

        usuarioService.guardarUsuario(usuario);
        status.setComplete();
        flash.addFlashAttribute("exito", "Usuario guardado correctamente");
        return "redirect:/usuarios";
    }

    @PostMapping("/procesar")
    public String procesarUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult errors, Model model,
            SessionStatus status, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("titulo", "Formulario de usuario");
            model.addAttribute("advertencia", "Por favor valide y complete los campos del formulario");
            return "vistas/usuarioForm";
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String ModifUsuario(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario == null) {
            flash.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/usuarios";
        }
        model.addAttribute("titulo", "Editar usuario");
        model.addAttribute("usuario", usuario);
        return "vistas/usuarioForm";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes flash) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario != null) {
            usuarioService.eliminar(id);
            flash.addFlashAttribute("exito", "Usuario eliminado correctamente");
        } else {
            flash.addFlashAttribute("error", "Usuario no encontrado");
        }
        return "redirect:/usuarios";
    }
}
