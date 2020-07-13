package com.example.findyourstyle.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Fragments.AgregarProductoFragment;
import com.example.findyourstyle.Modelo.CuentaTienda;
import com.example.findyourstyle.R;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class InicioSesionTienda extends AppCompatActivity {

    private EditText txtCorreoCliente,etxtPasswordCliente;

    private Button btnIniciarSesion;
    private  Button btnRegistrarTienda;
    private String correo;
    private String contrasenia;
    CuentaTienda ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion_cliente);

        txtCorreoCliente = (EditText) findViewById(R.id.etxtCorreoInicioSesionCliente);
        etxtPasswordCliente = (EditText) findViewById(R.id.etxt_PasswordCLiente);

        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesionCliente);
        btnRegistrarTienda = findViewById(R.id.btnRegistroNuevaTienda);
        recuperarPeferencias();

        final AgregarProductoFragment agregarProductoFragment = new AgregarProductoFragment();




        btnRegistrarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(InicioSesionTienda.this, RegistroTiendaActivity.class);
                startActivity(intent1);
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = txtCorreoCliente.getText().toString();
                contrasenia = etxtPasswordCliente.getText().toString();
                if(!correo.isEmpty() && !contrasenia.isEmpty()){
                    final String ip = getString(R.string.ip);
                    validarUsuario(ip+"/findyourstyleBDR/validar_cliente.php");
                }else {
                    Toast.makeText(InicioSesionTienda.this, "No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void validarUsuario(String URL){
        final String c;
        c = correo;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    guardarPreferencias();
                    Intent intent = new Intent(InicioSesionTienda.this, HomeTiendaActivity.class);
                    intent.putExtra("correoTienda", c);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(InicioSesionTienda.this,"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InicioSesionTienda.this,"Servidor no encontrado",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramentros = new HashMap<String, String>();
                paramentros.put("correo_tienda", correo);
                paramentros.put("contrasenia_tienda", contrasenia);
                return paramentros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public  void guardarPreferencias(){
        CuentaTienda cuentaTienda;
        AgregarProductoFragment agregarProductoFragment = new AgregarProductoFragment();
        SharedPreferences preferences = getSharedPreferences("preferensiaLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo_tienda", correo);

        editor.putString("contrasenia_tienda", contrasenia);
        editor.putBoolean("sesion", true);
        editor.commit();

    }

    public void recuperarPeferencias(){
        SharedPreferences preferences = getSharedPreferences("preferensiaLogin", Context.MODE_PRIVATE);
        txtCorreoCliente.setText(preferences.getString("correo_tienda", ""));
        etxtPasswordCliente.setText(preferences.getString("contrasenia_tienda", ""));
    }
}
