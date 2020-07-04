package com.example.findyourstyle.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.CategoriaTienda;
import com.example.findyourstyle.Modelo.Ciudad;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrarseActivity extends AppCompatActivity  {

    private ImageView volverAtras, btnImgPerfil, imgPerfil;
    private EditText nombre_usuario;
    private EditText apellido_usuario;
    private EditText correo_usuario;
    private EditText contrasenia_usuario;
    private EditText contrasenia2;
    private Button registrarse;
    private StringRequest stringRequest;

    // Pantalla de progreso
    ProgressDialog progreso;

    //Conexion con el web service
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private AsyncHttpClient asyncHttpClient;
    Spinner spCiudadUsuario;
    private Bitmap bitmap;
    String imagenUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View vista = inflater.inflate(R.layout.activity_registrarse, container, false);

        setContentView(R.layout.activity_registrarse);
        volverAtras = (ImageView) findViewById(R.id.imgVolverAtrasRegistro);
        btnImgPerfil = findViewById(R.id.btnAgregarImagenPerfil);
        imgPerfil = findViewById(R.id.imgPerfil);

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
                if(!nombre_usuario.getText().toString().isEmpty() && !apellido_usuario.getText().toString().isEmpty() && !correo_usuario.getText().toString().isEmpty() && !contrasenia_usuario.getText().toString().isEmpty() && !contrasenia2.getText().toString().isEmpty()){
                    if (validarEmail(correo_usuario.getText().toString())){
                        if (contrasenia_usuario.getText().toString().equals(contrasenia2.getText().toString())) {
                            if (bitmap != null) {
                                cargarWebService();
                            } else {
                                Toast.makeText(RegistrarseActivity.this, "Debe ingresar una imagen", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(RegistrarseActivity.this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        correo_usuario.setError("Email no válido");
                    }



                }else {
                    Toast.makeText(RegistrarseActivity.this, "No se permiten campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnImgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });


    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione una Apliacación"), 10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            Uri path;
            path =data.getData();
            imgPerfil.setImageURI(path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                imgPerfil.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap=redimensionarImagen(bitmap,800,800);
        }
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }


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

    private String convertirImagenString(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte [] imageByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imageByte,Base64.DEFAULT);


        return imagenString;
    }

    private void cargarWebService() {
        //Barra de progreso
        progreso = new ProgressDialog(this);
        progreso.setMessage("Cargando...");
        progreso.show();
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/wsJSONRegistro.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
                progreso.hide();
                nombre_usuario.setText("");
                apellido_usuario.setText("");
                correo_usuario.setText("");
                contrasenia_usuario.setText("");
                contrasenia2.setText("");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getApplicationContext(), "No se puede registrar" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nombreUsuario = nombre_usuario.getText().toString();
                String apellidoUsuario = apellido_usuario.getText().toString();
                String correoUsuario = correo_usuario.getText().toString();
                String contraseniaUsuario = contrasenia_usuario.getText().toString();
                String ciudad = spCiudadUsuario.getSelectedItem().toString();
                String imagenUsuario = convertirImagenString(bitmap);




                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre_usuario", nombreUsuario);
                parametros.put("apellido_usuario", apellidoUsuario);
                parametros.put("correo_usuario",correoUsuario );
                parametros.put("contrasenia_usuario", contraseniaUsuario);
                parametros.put("nombre_ciudad", ciudad);
                parametros.put("imagen", imagenUsuario);

                return parametros;
            }
        };
        request.add(stringRequest);


    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}