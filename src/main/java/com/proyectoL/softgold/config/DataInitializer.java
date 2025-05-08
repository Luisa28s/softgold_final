package com.proyectoL.softgold.config;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.repository.RolDAO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    // Inicializacion de los roles en la base de datos al iniciar la aplicacion
    @Autowired
    private RolDAO rolDAO;

    @PostConstruct
    public void initRoles() {
        crearRolSiNoExiste("ADMINISTRADOR");
        crearRolSiNoExiste("USUARIO");
        crearRolSiNoExiste("SUPERADMIN");
    }

    private void crearRolSiNoExiste(String nombreRol) {
        if (rolDAO.findByNombre(nombreRol) == null) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolDAO.save(rol);
            System.out.println("Rol creado: " + nombreRol);
        }
    }
}
