package com.proyectoL.softgold.service;

import java.util.List;
import java.util.Optional;

import com.proyectoL.softgold.model.Mina;

public interface MinaServiceIface {
    List<Mina> obtenerTodos();

    Optional<Mina> buscarPorId(Long id);

    Mina guardar(Mina mina);

    void eliminar(Long id);

}
