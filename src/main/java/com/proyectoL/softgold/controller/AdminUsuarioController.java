package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//Controlador de los administradores 
@RestController
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    public String crearUsuario(@Valid @RequestBody Usuario usuario) {
        usuarioService.guardarUsuario(usuario);
        return "Usuario creado correctamente";
    }

    @PutMapping("/{id}")
    public String actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre1(usuarioActualizado.getNombre1());
            usuario.setNombre2(usuarioActualizado.getNombre2());
            usuario.setApellido1(usuarioActualizado.getApellido1());
            usuario.setApellido2(usuarioActualizado.getApellido2());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setPassword(usuarioActualizado.getPassword());
            usuario.setRoles(usuarioActualizado.getRoles());
            usuarioService.guardarUsuario(usuario);
            return "Usuario actualizado correctamente";
        } else {
            return "Usuario no encontrado";
        }
    }

    @DeleteMapping("/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "Usuario eliminado correctamente";
    }
}
