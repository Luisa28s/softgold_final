package com.proyectoL.softgold.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectoL.softgold.model.Mina;
import com.proyectoL.softgold.repository.MinaDAO;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MinaService implements MinaServiceIface {

    @Autowired
    private MinaDAO minaDAO;

    public MinaService(MinaDAO minaDAO) {
        this.minaDAO = minaDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mina> obtenerTodos() {
        return minaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mina> buscarPorId(Long id) {
        return minaDAO.findById(id);
    }

    @Override
    @Transactional
    public Mina guardar(Mina mina) {
        return minaDAO.save(mina);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        minaDAO.deleteById(id);
    }
}