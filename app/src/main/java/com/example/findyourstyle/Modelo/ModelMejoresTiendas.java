package com.example.findyourstyle.Modelo;

public class ModelMejoresTiendas {

    private String nombreTienda;
    private String puntos;
    private String estrellas;
    private int imgMejoresTiendas;

    public ModelMejoresTiendas(){}

    public ModelMejoresTiendas(String nombreTienda, String puntos, String estrellas, int imgMejoresTiendas) {
        this.nombreTienda = nombreTienda;
        this.puntos = puntos;
        this.estrellas = estrellas;
        this.imgMejoresTiendas = imgMejoresTiendas;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(String estrellas) {
        this.estrellas = estrellas;
    }

    public int getImgMejoresTiendas() {
        return imgMejoresTiendas;
    }

    public void setImgMejoresTiendas(int imgMejoresTiendas) {
        this.imgMejoresTiendas = imgMejoresTiendas;
    }
}
