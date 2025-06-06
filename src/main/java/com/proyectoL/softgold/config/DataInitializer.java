package com.proyectoL.softgold.config;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.RolDAO;
import com.proyectoL.softgold.repository.UsuarioDAO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private RolDAO rolDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Crear el rol ADMINISTRADOR si no existe
        Rol rolAdmin = rolDAO.findByNombre("ADMINISTRADOR");
        if (rolAdmin == null) {
            rolAdmin = new Rol();
            rolAdmin.setNombre("ADMINISTRADOR");
            rolDAO.save(rolAdmin);
        }

        Rol rolMinero = rolDAO.findByNombre("MINERO");
        if (rolMinero == null) {
            rolMinero = new Rol();
            rolMinero.setNombre("MINERO");
            rolDAO.save(rolMinero);
        }

        Rol rolEmpleado = rolDAO.findByNombre("EMPLEADO");
        if (rolEmpleado == null) {
            rolEmpleado = new Rol();
            rolEmpleado.setNombre("EMPLEADO");
            rolDAO.save(rolEmpleado);
        }
        Rol rolUsuario = rolDAO.findByNombre("USUARIO");
        if (rolUsuario == null) {
            rolUsuario = new Rol();
            rolUsuario.setNombre("USUARIO");
            rolDAO.save(rolUsuario);
        }

        // Crear un usuario administrador si no existe
        Optional<Usuario> adminOpt = usuarioDAO.findByEmail("admin@softgold.com");
        if (adminOpt.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre1("Admin");
            admin.setNombre2("Default");
            admin.setApellido1("Softgold");
            admin.setApellido2("Default");
            admin.setEmail("admin@softgold.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setTipoUsuario("ADMINISTRADOR");
            admin.setTipoDocumento("Cédula de ciudadanía");
            admin.setCedula("123456789");
            admin.setRoles(List.of(rolAdmin));
            usuarioDAO.save(admin);
        }
    }
}
