package com.example.findyourstyle.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Fragments.InicioFragment;
import com.example.findyourstyle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    private EditText  etxtCorreo;
    private EditText etxtContrasenia;
    private String correo;
    private String contrasenia;

    BottomNavigationView bottomNavigationView;
    Button btnIniciarSesion, btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistro);

        etxtCorreo= findViewById(R.id.etxtCorreoInicioSesion);
        etxtContrasenia= findViewById(R.id.etxtContraseniaInicioSesion);

        bottomNavigationView    =  findViewById(R.id.bottomNavegation);

        recuperarPeferencias();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = etxtCorreo.getText().toString();
                contrasenia = etxtContrasenia.getText().toString();
                if(!correo.isEmpty() && !contrasenia.isEmpty()){
                    final String ip = getString(R.string.ip);
                    validarUsuario(ip+"/findyourstyleBDR/validar_usuario.php");
                }else {
                    Toast.makeText(MainActivity.this, "No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrarseActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    guardarPreferencias();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this,"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramentros = new HashMap<String, String>();
                paramentros.put("correo_usuario", correo);
                paramentros.put("contrasenia_usuario", contrasenia);
                return paramentros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferensiaLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo_usuario", correo);
        editor.putString("contrasenia_usuario", contrasenia);
        editor.putBoolean("sesion", true);
        editor.commit();
    }

    private void recuperarPeferencias(){
        SharedPreferences preferences = getSharedPreferences("preferensiaLogin", Context.MODE_PRIVATE);
        etxtCorreo.setText(preferences.getString("correo_usuario", ""));
        etxtContrasenia.setText(preferences.getString("contrasenia_usuario", ""));

    }
   }
