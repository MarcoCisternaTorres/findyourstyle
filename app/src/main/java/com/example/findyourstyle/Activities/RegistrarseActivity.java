package com.example.findyourstyle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;

import org.json.JSONObject;

public class RegistrarseActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    ImageView volverAtras;
    private EditText nombre_usuario;
    private EditText apellido_usuario;
    private EditText correo_usuario;
    private EditText contrasenia_usuario;
    private EditText contrasenia2;
    private Button registrarse;

    // Pantalla de progreso
    ProgressDialog progreso;

    //Conexion con el web service
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View vista = inflater.inflate(R.layout.activity_registrarse, container, false);

        setContentView(R.layout.activity_registrarse);
        volverAtras = (ImageView) findViewById(R.id.imgVolverAtrasRegistro);
        nombre_usuario = (EditText) findViewById(R.id.etxtNombreUsuarioRegistro);
        apellido_usuario = (EditText) findViewById(R.id.etxApellidoUsuarioRegistro);
        correo_usuario = (EditText) findViewById(R.id.etxtCorreoRegistro);
        contrasenia_usuario = (EditText) findViewById(R.id.etxtContraseniaRegistro);
        contrasenia2 = (EditText) findViewById(R.id.etxtContrasenia2Registro);
        registrarse = (Button) findViewById(R.id.btnRegistrarse);

        request = Volley.newRequestQueue(this);


        volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contrasenia_usuario.getText().toString().equals(contrasenia2.getText().toString())){
                    cargarWebService();
                }else {
                    Toast.makeText(RegistrarseActivity.this,"Las contraseñas no son igual", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void cargarWebService(){
        //Barra de progreso
        progreso = new ProgressDialog(this);
        progreso.setMessage("Cargando...");
        progreso.show();
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip +"/findyourstyleBDR/wsJSONRegistro.php?nombre_usuario="+nombre_usuario.getText().toString()+
                "&apellido_usuario="+apellido_usuario.getText().toString()+"&correo_usuario="+correo_usuario.getText().toString()+"&contrasenia_usuario="+contrasenia_usuario.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this,"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        progreso.hide();
        nombre_usuario.setText("");
        apellido_usuario.setText("");
        correo_usuario.setText("");
        contrasenia_usuario.setText("");
        contrasenia2.setText("");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(this,"No se puede registrar"+error.toString(),Toast.LENGTH_SHORT).show();
        //Long.bitCount(i,"ERROR",error.toString());

    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
