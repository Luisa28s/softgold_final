package com.proyectoL.softgold.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "mapa")
public class Mapa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_mapa")
    private Long codigoMapa;

    @Column(nullable = false)
    private String coordenadas;

    @Column(nullable = false)
    private String descripcion;

    // Relación N:M con Mina
    @ManyToMany(mappedBy = "mapas")
    private List<Mina> minas;

    public Long getCodigoMapa() {
        return codigoMapa;
    }

    public void setCodigoMapa(Long codigoMapa) {
        this.codigoMapa = codigoMapa;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Mina> getMinas() {
        return minas;
    }

    public void setMinas(List<Mina> minas) {
        this.minas = minas;
    }

    public Mapa() {
    }

    public Mapa(Long codigoMapa, String coordenadas, String descripcion, List<Mina> minas) {
        this.codigoMapa = codigoMapa;
        this.coordenadas = coordenadas;
        this.descripcion = descripcion;
        this.minas = minas;
    }

    @Override
    public String toString() {
        return "Mapa [codigoMapa=" + codigoMapa + ", coordenadas=" + coordenadas + ", descripcion=" + descripcion
                + ", minas=" + minas + "]";
    }

}
