package com.example.findyourstyle.Modelo;

import java.io.Serializable;

public class CategoriaTienda implements Serializable {

    private int idCategoriaTienda;
    private  String nombreCategoriaTienda;

    public CategoriaTienda(int idCategoriaTienda, String nombreCategoriaTienda) {
        this.idCategoriaTienda = idCategoriaTienda;
        this.nombreCategoriaTienda = nombreCategoriaTienda;
    }

    public CategoriaTienda() {
    }

    public int getIdCategoriaTienda() {
        return idCategoriaTienda;
    }

    public void setIdCategoriaTienda(int idCategoriaTienda) {
        this.idCategoriaTienda = idCategoriaTienda;
    }

    public String getNombreCategoriaTienda() {
        return nombreCategoriaTienda;
    }

    public void setNombreCategoriaTienda(String nombreCategoriaTienda) {
        this.nombreCategoriaTienda = nombreCategoriaTienda;
    }

    @Override
    public String toString() {
        return nombreCategoriaTienda;
    }
}
