package com.proyectoL.softgold.config;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.RolDAO;
import com.proyectoL.softgold.repository.UsuarioDAO;

//Inicializacion del superAdmin en la base de datos al iniciar la aplicacion
@Component
public class SuperAdminInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private RolDAO rolDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Verificar si ya existe un superadmin
        Optional<Usuario> existente = usuarioDAO.findByEmail("superadmin@softgold.com");
        if (existente.isPresent()) {
            return;
        }

        // Asegurarse de que el rol SUPERADMIN exista
        Rol rolSuperadmin = rolDAO.findByNombre("SUPERADMIN");
        if (rolSuperadmin == null) {
            rolSuperadmin = new Rol();
            rolSuperadmin.setNombre("SUPERADMIN");
            rolDAO.save(rolSuperadmin);
        }

        Usuario superadmin = new Usuario();
        superadmin.setNombre1("Super");
        superadmin.setNombre2("");
        superadmin.setApellido1("Admin");
        superadmin.setApellido2("");
        superadmin.setEmail("superadmin@softgold.com");
        superadmin.setPassword(passwordEncoder.encode("superSegura123"));
        superadmin.setRoles(Collections.singleton(rolSuperadmin));
        superadmin.setBloqueado(false);
        superadmin.setIntentosFallidos(0);

        usuarioDAO.save(superadmin);

        System.out.println(">>> Usuario SUPERADMIN creado correctamente");
    }
}
