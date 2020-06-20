package com.example.findyourstyle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.CategoriaTienda;
import com.example.findyourstyle.Modelo.Ciudad;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    private AsyncHttpClient asyncHttpClient;
    Spinner spCiudadUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View vista = inflater.inflate(R.layout.activity_registrarse, container, false);

        setContentView(R.layout.activity_registrarse);
        volverAtras = (ImageView) findViewById(R.id.imgVolverAtrasRegistro);

        nombre_usuario = findViewById(R.id.etxtNombreUsuarioRegistro);
        apellido_usuario = findViewById(R.id.etxApellidoUsuarioRegistro);
        correo_usuario =  findViewById(R.id.etxtCorreoRegistro);
        contrasenia_usuario =  findViewById(R.id.etxtContraseniaRegistro);
        contrasenia2 =  findViewById(R.id.etxtContrasenia2Registro);
        registrarse =  findViewById(R.id.btnRegistrarse);
        spCiudadUsuario = findViewById(R.id.spinnerCiudadUsuario);
        asyncHttpClient = new AsyncHttpClient();
        llenarSpinnner();

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
                if (contrasenia_usuario.getText().toString().equals(contrasenia2.getText().toString())) {
                    cargarWebService();
                } else {
                    Toast.makeText(RegistrarseActivity.this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void cargarWebService() {
        //Barra de progreso
        progreso = new ProgressDialog(this);
        progreso.setMessage("Cargando...");
        progreso.show();
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/wsJSONRegistro.php?nombre_usuario=" + nombre_usuario.getText().toString() +
                "&apellido_usuario=" + apellido_usuario.getText().toString() + "&correo_usuario=" + correo_usuario.getText().toString() + "&contrasenia_usuario=" + contrasenia_usuario.getText().toString() + "&nombre_ciudad="+spCiudadUsuario.getSelectedItem().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this, "Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "No se puede registrar" + error.toString(), Toast.LENGTH_SHORT).show();
        //Long.bitCount(i,"ERROR",error.toString());

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void llenarSpinnner(){
        final String ip1 = getString(R.string.ip);
        String url = ip1 + "/findyourstyleBDR/wsJSONCiudad.php";
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    cargarSpinner(new String(responseBody));
                }else{

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void cargarSpinner(String respuesta){
        ArrayList<Ciudad> listaCiudad = new ArrayList<Ciudad>();
        try {
            JSONObject jsonObj = new JSONObject(respuesta);
            JSONArray jsonArreglo =  jsonObj.optJSONArray("ciudad");
            for (int i = 0; i < jsonArreglo.length(); i++){
                Ciudad c = new Ciudad();
                c.setNombreCiudad(jsonArreglo.getJSONObject(i).optString("nombre_ciudad"));
                listaCiudad.add(c);
            }
            ArrayAdapter<Ciudad> cu = new ArrayAdapter<Ciudad>(RegistrarseActivity.this,android.R.layout.simple_dropdown_item_1line, listaCiudad);
            spCiudadUsuario.setAdapter(cu);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}