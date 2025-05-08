package com.proyectoL.softgold.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectoL.softgold.model.PasswordResetToken;
import com.proyectoL.softgold.model.Usuario;

@Repository
public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUsuarioId(Long id);

    Optional<PasswordResetToken> findByUsuario(Usuario usuario);

}
