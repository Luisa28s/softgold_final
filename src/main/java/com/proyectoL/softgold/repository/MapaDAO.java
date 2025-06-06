package com.proyectoL.softgold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectoL.softgold.model.Mapa;

@Repository
public interface MapaDAO extends JpaRepository<Mapa, Long> {
    Mapa findByCoordenadas(String coordenadas);

    Mapa findByCodigoMapa(Long codigoMapa);

    List<Mapa> findByDescripcionContainingIgnoreCase(String descripcion);

}
