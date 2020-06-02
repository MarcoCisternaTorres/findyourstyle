package com.example.findyourstyle.Modelo;

import java.io.Serializable;

public class ModeloBuscar implements Serializable {

    private String  nombreProducto;
    private String  tienda;
    private String  direccion;
    private String  precio;
    private int     idImagenBuscar;

    public ModeloBuscar(){}

    public ModeloBuscar(String nombreProducto, String tienda,String direccion, String precio, int idImagenBuscar) {
        this.nombreProducto = nombreProducto;
        this.tienda         = tienda;
        this.direccion      = direccion;
        this.precio         = precio;
        this.idImagenBuscar = idImagenBuscar;
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

    public int getIdImagenBuscar() {
        return idImagenBuscar;
    }

    public void setIdImagenBuscar(int idImagenBuscar) {
        this.idImagenBuscar = idImagenBuscar;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
