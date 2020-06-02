package com.example.findyourstyle.Modelo;

public class HorasAtencion {
    private String fecha;
    private String hora;

    public HorasAtencion() {
    }

    public HorasAtencion(String fecha, String hora) {
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
