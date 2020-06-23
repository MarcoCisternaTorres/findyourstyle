package com.example.findyourstyle.Modelo;

public class CuentaTienda {
    private String correoTienda;
    private String passTienda;

    public CuentaTienda() {
    }

    public CuentaTienda(String correoTienda, String passTienda) {
        this.correoTienda = correoTienda;
        this.passTienda = passTienda;
    }

    public String getCorreoTienda() {
        return correoTienda;
    }

    public void setCorreoTienda(String correoTienda) {
        this.correoTienda = correoTienda;
    }

    public String getPassTienda() {
        return passTienda;
    }

    public void setPassTienda(String passTienda) {
        this.passTienda = passTienda;
    }

    @Override
    public String toString() {
        return correoTienda;
    }
}
