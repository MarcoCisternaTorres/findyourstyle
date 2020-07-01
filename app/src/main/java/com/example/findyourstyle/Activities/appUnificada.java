package com.example.findyourstyle.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.findyourstyle.R;

public class appUnificada extends AppCompatActivity {

    private Button btnBuscarTiendas;
    private Button btnPromocionarTiendas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_unificada);

        btnBuscarTiendas = findViewById(R.id.btn_BuscarTiendasEsteticas);
        btnPromocionarTiendas = findViewById(R.id.btn_promocionarTienda);

        btnPromocionarTiendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InicioSesionTienda.class);
                startActivityForResult(intent,0);
            }
        });

        btnBuscarTiendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent1,0);
            }
        });
    }
}
