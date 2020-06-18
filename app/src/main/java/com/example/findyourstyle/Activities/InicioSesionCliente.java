package com.example.findyourstyle.Activities;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.findyourstyle.R;

import java.util.HashMap;
import java.util.Map;

public class InicioSesionCliente extends AppCompatActivity {

    private EditText txtCorreoCliente,etxtPasswordCliente;

    private Button btnIniciarSesion;

    private String correo;
    private String contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion_cliente);

        txtCorreoCliente = (EditText) findViewById(R.id.etxtCorreoInicioSesionCliente);
        etxtPasswordCliente = (EditText) findViewById(R.id.etxt_PasswordCLiente);

        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesionCliente);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = txtCorreoCliente.getText().toString();
                contrasenia = etxtPasswordCliente.getText().toString();

                if(!correo.isEmpty() && !contrasenia.isEmpty()){
                    final String ip = getString(R.string.ip);
                    validarUsuario(ip+"/findyourstyleBDR/validar_cliente.php");
                }else {
                    Toast.makeText(InicioSesionCliente.this, "No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    guardarPreferencias();
                    Intent intent = new Intent(InicioSesionCliente.this, HomeClienteActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(InicioSesionCliente.this,"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InicioSesionCliente.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramentros = new HashMap<String, String>();
                paramentros.put("correo_cliente", correo);
                paramentros.put("contrasenia_cliente", contrasenia);
                return paramentros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferensiaLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo_cliente", correo);
        editor.putString("contrasenia_cliente", contrasenia);
        editor.putBoolean("sesion", true);
        editor.commit();
    }

    private void recuperarPeferencias(){
        SharedPreferences preferences = getSharedPreferences("preferensiaLogin", Context.MODE_PRIVATE);
        txtCorreoCliente.setText(preferences.getString("correo_cliente", ""));
        etxtPasswordCliente.setText(preferences.getString("contrasenia_cliente", ""));

    }
}
