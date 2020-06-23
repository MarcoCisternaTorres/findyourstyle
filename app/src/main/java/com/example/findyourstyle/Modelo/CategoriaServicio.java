package com.example.findyourstyle.Modelo;

import java.io.Serializable;

public class CategoriaServicio implements Serializable {

    private int icCategoriaServicio;
    private String nombreCategoriaServicio;

    public CategoriaServicio() {
    }

    public CategoriaServicio(int icCategoriaServicio, String nombreCategoriaServicio) {
        this.icCategoriaServicio = icCategoriaServicio;
        this.nombreCategoriaServicio = nombreCategoriaServicio;
    }

    public int getIcCategoriaServicio() {
        return icCategoriaServicio;
    }

    public void setIcCategoriaServicio(int icCategoriaServicio) {
        this.icCategoriaServicio = icCategoriaServicio;
    }

    public String getNombreCategoriaServicio() {
        return nombreCategoriaServicio;
    }

    public void setNombreCategoriaServicio(String nombreCategoriaServicio) {
        this.nombreCategoriaServicio = nombreCategoriaServicio;
    }

    @Override
    public String toString() {
        return nombreCategoriaServicio;
    }
}
