package com.example.findyourstyle.Interfaces;

import com.example.findyourstyle.Modelo.ModelLoMasBuscado;
import com.example.findyourstyle.Modelo.ModeloBuscar;

public interface IComunicaFragment {
    public void enviarProducto(ModeloBuscar modeloBuscar);
    public  void enviarProductosMasBuscados(ModelLoMasBuscado modelLoMasBuscado);

}
