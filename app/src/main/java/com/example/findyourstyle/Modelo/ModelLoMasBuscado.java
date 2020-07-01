package com.example.findyourstyle.Modelo;

import java.io.Serializable;

public class ModelLoMasBuscado implements Serializable {

    private String  nombreProducto;
    private String  tienda;
    private String  precio;
    private int     idImagenLoMasBuscado;

    public ModelLoMasBuscado(){}

    public ModelLoMasBuscado(String nombreProducto, String tienda, String precio, int idImagenBuscar) {
        this.nombreProducto = nombreProducto;
        this.tienda = tienda;
        this.precio = precio;
        this.idImagenLoMasBuscado = idImagenBuscar;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getIdImagenLoMasBuscado() {
        return idImagenLoMasBuscado;
    }

    public void setIdImagenLoMasBuscado(int idImagenBuscar) {
        this.idImagenLoMasBuscado = idImagenBuscar;
    }
}
