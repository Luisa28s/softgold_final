package com.proyectoL.softgold.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//Controlador que maneja las búsquedas desde la barra de busqueda
public class BusquedaController {

    @GetMapping("/api/busqueda")
    @ResponseBody
    public List<String> buscar(@RequestParam String q) {
        // Simulación de búsqueda (Hay que modificarla)
        List<String> datos = List.of(
                "Cliente A", "Cliente B", "Producto X", "Producto Y", "Factura #123", "Factura #456");

        return datos.stream()
                .filter(item -> item.toLowerCase().contains(q.toLowerCase()))
                .collect(Collectors.toList());
    }
}