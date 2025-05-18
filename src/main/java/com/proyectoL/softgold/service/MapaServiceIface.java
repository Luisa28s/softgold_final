package com.proyectoL.softgold.service;

import java.util.List;
import java.util.Optional;

import com.proyectoL.softgold.model.Mapa;

public interface MapaServiceIface {
    List<Mapa> obtenerTodos();

    Optional<Mapa> buscarPorId(Long id);

    Mapa guardar(Mapa mina);

    void eliminar(Long id);

}
