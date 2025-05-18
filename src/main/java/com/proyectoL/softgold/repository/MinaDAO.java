package com.proyectoL.softgold.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectoL.softgold.model.Mina;

@Repository
public interface MinaDAO extends JpaRepository<Mina, Long> {
    Mina findByNombre(String nombre);

    Mina findByCodMina(Long codMina);

}
