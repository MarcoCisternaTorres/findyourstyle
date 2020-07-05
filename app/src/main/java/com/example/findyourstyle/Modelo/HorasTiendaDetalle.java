package com.example.findyourstyle.Modelo;

import java.io.Serializable;

public class HorasTiendaDetalle implements Serializable {

    private String fecha_atencion;
    private String hora_atencion;

    public HorasTiendaDetalle() {
    }

    public HorasTiendaDetalle(String fecha_atencion, String hora_atencion) {
        this.fecha_atencion = fecha_atencion;
        this.hora_atencion = hora_atencion;
    }

    public String getFecha_atencion() {
        return fecha_atencion;
    }

    public void setFecha_atencion(String fecha_atencion) {
        this.fecha_atencion = fecha_atencion;
    }

    public String getHora_atencion() {
        return hora_atencion;
    }

    public void setHora_atencion(String hora_atencion) {
        this.hora_atencion = hora_atencion;
    }
}
