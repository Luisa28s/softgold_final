package com.proyectoL.softgold.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectoL.softgold.model.Mapa;
import com.proyectoL.softgold.repository.MapaDAO;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MapaService implements MapaServiceIface {
    @Autowired
    private MapaDAO mapaDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Mapa> obtenerTodos() {
        return mapaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mapa> buscarPorId(Long id) {
        return mapaDAO.findById(id);
    }

    @Override
    @Transactional
    public Mapa guardar(Mapa mapa) {
        return mapaDAO.save(mapa);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        mapaDAO.deleteById(id);
    }

}
