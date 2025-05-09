package com.proyectoL.softgold.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.model.Usuario;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRolesIn(List<Rol> roles);

    boolean existsByEmail(String email);

    Optional<Usuario> findById(Long id);

    boolean existsById(Long id);

    List<Usuario> findByTipoUsuario(String tipoUsuario);
}
