package com.proyectoL.softgold.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyectoL.softgold.repository.UsuarioDAO;

@RestController
@RequestMapping("/api/validar")
public class ValidacionController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/cedula")
    public boolean cedulaExiste(@RequestParam String cedula) {
        return usuarioDAO.existsByCedula(cedula);
    }

    @GetMapping("/email")
    public boolean emailExiste(@RequestParam String email) {
        return usuarioDAO.existsByEmail(email);
    }
}