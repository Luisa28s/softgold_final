package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.UsuarioDAO;
import com.proyectoL.softgold.repository.MinaDAO;
import com.proyectoL.softgold.repository.RolDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/usuarios/empleados")
public class EmpleadoController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private RolDAO rolDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinaDAO minaDAO;

    // Listar empleados
    @GetMapping("")
    public String listarEmpleados(Model model) {
        List<Usuario> empleados = usuarioDAO.findByTipoUsuario("EMPLEADO");
        model.addAttribute("listaUsuarios", empleados);
        model.addAttribute("tipoUsuario", "EMPLEADO");
        return "vistas/listarEmpleados";
    }

    // Mostrar formulario para crear un empleado
    @GetMapping("/crear")
    public String mostrarFormularioCrearEmpleado(Model model) {
        Usuario usuario = new Usuario();
        usuario.setTipoUsuario("EMPLEADO");
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Crear Empleado");
        model.addAttribute("minas", minaDAO.findAll());
        return "vistas/crearEmpleado";
    }

    // Procesar la creación de un empleado
    @PostMapping("/crear")
    public String crearEmpleado(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            model.addAttribute("minas", minaDAO.findAll());
            model.addAttribute("titulo", "Crear Empleado");
            return "vistas/crearEmpleado";
        }

        Rol rolEmpleado = rolDAO.findByNombre("EMPLEADO");
        if (rolEmpleado == null) {
            redirectAttrs.addFlashAttribute("error", "El rol 'EMPLEADO' no existe en la base de datos.");
            return "redirect:/admin/usuarios/empleados";
        }

        if (usuarioDAO.existsByCedula(usuario.getCedula())) {
            model.addAttribute("minas", minaDAO.findAll());
            model.addAttribute("titulo", "Crear Empleado");
            model.addAttribute("errorCedula", "Ya existe un empleado con la cédula ingresada.");
            return "vistas/crearEmpleado";
        }

        if (usuarioDAO.existsByEmail(usuario.getEmail())) {
            model.addAttribute("minas", minaDAO.findAll());
            model.addAttribute("titulo", "Crear Empleado");
            model.addAttribute("errorEmail", "Ya existe un empleado con el correo ingresado.");
            return "vistas/crearEmpleado";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlano()));
        usuario.setRoles(Collections.singleton(rolEmpleado));
        usuario.setBloqueado(false);
        usuario.setIntentosFallidos(0);

        usuarioDAO.save(usuario);
        redirectAttrs.addFlashAttribute("exito", "Empleado creado correctamente.");
        return "redirect:/admin/usuarios/empleados";
    }

    // Mostrar formulario para editar un empleado
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarEmpleado(@PathVariable Long id, Model model,
            RedirectAttributes redirectAttrs) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findById(id);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getTipoUsuario().equals("EMPLEADO")) {
            redirectAttrs.addFlashAttribute("error", "Empleado no encontrado.");
            return "redirect:/admin/usuarios/empleados";
        }

        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("titulo", "Editar Empleado");
        model.addAttribute("minas", minaDAO.findAll()); // <-- agrega esto

        return "vistas/editarEmpleado";
    }

    // Procesar la edición de un empleado
    @PostMapping("/editar/{id}")
    public String procesarEditarEmpleado(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("titulo", "Editar Empleado");
            model.addAttribute("minas", minaDAO.findAll());
            return "vistas/editarEmpleado";
        }

        Optional<Usuario> usuarioOpt = usuarioDAO.findById(id);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getTipoUsuario().equals("EMPLEADO")) {
            redirectAttrs.addFlashAttribute("error", "Empleado no encontrado.");
            return "redirect:/admin/usuarios/empleados";
        }

        Usuario empleadoExistente = usuarioOpt.get();
        empleadoExistente.setNombre1(usuario.getNombre1());
        empleadoExistente.setNombre2(usuario.getNombre2());
        empleadoExistente.setApellido1(usuario.getApellido1());
        empleadoExistente.setApellido2(usuario.getApellido2());
        empleadoExistente.setEmail(usuario.getEmail());
        empleadoExistente.setTelefono(usuario.getTelefono());
        empleadoExistente.setMina(usuario.getMina());

        if (usuario.getPasswordPlano() != null && !usuario.getPasswordPlano().isEmpty()) {
            empleadoExistente.setPassword(passwordEncoder.encode(usuario.getPasswordPlano()));
        }

        usuarioDAO.save(empleadoExistente);
        redirectAttrs.addFlashAttribute("exito", "Empleado actualizado correctamente.");
        return "redirect:/admin/usuarios/empleados";
    }

    // Eliminar un empleado
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findById(id);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getTipoUsuario().equals("EMPLEADO")) {
            redirectAttrs.addFlashAttribute("error", "Empleado no encontrado.");
            return "redirect:/admin/usuarios/empleados";
        }

        usuarioDAO.deleteById(id);
        redirectAttrs.addFlashAttribute("exito", "Empleado eliminado correctamente.");
        return "redirect:/admin/usuarios/empleados";
    }

    // Buscar empleados por cédula
    @GetMapping("/buscar")
    public String buscarEmpleadosPorCedula(@RequestParam("cedula") String cedula, Model model) {
        List<Usuario> empleados;
        if (cedula == null || cedula.trim().isEmpty()) {
            empleados = usuarioDAO.findByTipoUsuario("EMPLEADO");
        } else {
            Usuario empleado = usuarioDAO.findByCedulaAndTipoUsuario(cedula, "EMPLEADO");
            empleados = empleado != null ? List.of(empleado) : List.of();
        }
        model.addAttribute("listaUsuarios", empleados);
        model.addAttribute("tipoUsuario", "EMPLEADO");
        return "vistas/listarEmpleados";
    }

}
