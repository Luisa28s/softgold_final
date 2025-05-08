package com.proyectoL.softgold.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectoL.softgold.model.Rol;

@Repository
public interface RolDAO extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);

}
