package com.example.findyourstyle.Activities;

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
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.findyourstyle.R;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistroTiendaActivity extends AppCompatActivity {

    private EditText nombreTienda;
    private EditText direccionTienda;
    private EditText correoTienda;
    private EditText contraseniaTienda;
    private EditText contraseniaTienda2;
    ImageView imgVolverAtras;

    private Button btnRegistrarTienda;

    // Pantalla de progreso
    ProgressDialog progreso;

    //Conexion con el web service
    RequestQueue request, requestSpinner;
    JsonObjectRequest jsonObjectRequest, jsonObjectRequestSpninner;

    //Conexion con el web service para el spinner categoria tienda
    private AsyncHttpClient categoriaTienda;
    Spinner spCategoriaTienda;

    //Conexion con el web service prara el spinner ciudad
    private AsyncHttpClient ciudadTienda;
    Spinner spCiudadTienda;
    ImageView imgAgregarPerfilTienda, imgPerfilTienda;
    private Bitmap bitmap;
    String imagenTienda;
    private StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tienda);

        nombreTienda = findViewById(R.id.etxtNombreTiendaRegistro);
        direccionTienda = findViewById(R.id.txtDireccionTienda);
        correoTienda = findViewById(R.id.etxtCorreoRegistroTienda);
        contraseniaTienda = findViewById(R.id.etxtContraseniaRegistroTienda);
        contraseniaTienda2 = findViewById(R.id.etxtContrasenia2RegistroTienda);
        imgAgregarPerfilTienda =findViewById(R.id.imgAgregarImagenPerfilTienda);
        imgPerfilTienda = findViewById(R.id.imgAgregarPerfilTienda);
        imgVolverAtras = findViewById(R.id.imgVolverAtrasRegistroTienda);

        btnRegistrarTienda = findViewById(R.id.btnRegistrarTienda);

        request = Volley.newRequestQueue(this);

        categoriaTienda = new AsyncHttpClient();
        spCategoriaTienda = findViewById(R.id.spinnerCetgoriaTienda);
        spCiudadTienda = findViewById(R.id.spinnerCiudadTienda);

        llenarSpinnner();
        llenarSpinnnerCiudad();

        imgAgregarPerfilTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

        btnRegistrarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombreTienda.getText().toString().isEmpty() && !direccionTienda.getText().toString().isEmpty() && !contraseniaTienda.getText().toString().isEmpty() && !contraseniaTienda2.getText().toString().isEmpty() && !correoTienda.getText().toString().isEmpty()) {

                        if (contraseniaTienda.getText().toString().equals(contraseniaTienda.getText().toString())) {
                            if (bitmap != null) {
                                cargarWebService();
                            } else {
                                Toast.makeText(RegistroTiendaActivity.this, "Debe ingresar una imagen", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegistroTiendaActivity.this, "Las contraseñas no son igual", Toast.LENGTH_SHORT).show();
                        }

                    
                }else {
                    Toast.makeText(RegistroTiendaActivity.this, "Existen campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroTiendaActivity.this, InicioSesionTienda.class);
                startActivity(intent);
                finish();
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
            imgPerfilTienda.setImageURI(path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                imgPerfilTienda.setImageBitmap(bitmap);
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

    private void cargarWebService() {
        //Barra de progreso
        progreso = new ProgressDialog(this);
        progreso.setMessage("Cargando...");
        progreso.show();
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/wsJSONRegistroTienda.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
                progreso.hide();
                nombreTienda.setText("");
                direccionTienda.setText("");
                correoTienda.setText("");
                contraseniaTienda.setText("");
                contraseniaTienda2.setText("");

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
                String nombreT = nombreTienda.getText().toString();
                String direccionT= direccionTienda.getText().toString();
                String correoT = correoTienda.getText().toString();
                String contraseniaT = contraseniaTienda.getText().toString();
                String categoriaT = spCategoriaTienda.getSelectedItem().toString();
                String ciudad = spCiudadTienda.getSelectedItem().toString();
                String imagenT = convertirImagenString(bitmap);




                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre_tienda", nombreT);
                parametros.put("correo_tienda", correoT);
                parametros.put("contrasenia_tienda",contraseniaT );
                parametros.put("direccion_tienda", direccionT);
                parametros.put("nombre_categoria", categoriaT);
                parametros.put("nombre_ciudad", ciudad);
                parametros.put("imagen", imagenT);

                return parametros;
            }
        };
        request.add(stringRequest);


    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void llenarSpinnner(){
        final String ip1 = getString(R.string.ip);
        String url = ip1 + "/findyourstyleBDR/wsJSONCategoriaTienda.php";
        categoriaTienda.get(url, new AsyncHttpResponseHandler() {
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
        ArrayList<CategoriaTienda> listaCategoriaTienda = new ArrayList<CategoriaTienda>();
        try {
            JSONObject jsonObj = new JSONObject(respuesta);
            JSONArray jsonArreglo =  jsonObj.optJSONArray("categoria");
            for (int i = 0; i < jsonArreglo.length(); i++){
                CategoriaTienda c = new CategoriaTienda();
                c.setNombreCategoriaTienda(jsonArreglo.getJSONObject(i).optString("nombre_categoria"));
                listaCategoriaTienda.add(c);
            }
            ArrayAdapter<CategoriaTienda> ct = new ArrayAdapter<CategoriaTienda>(RegistroTiendaActivity.this,android.R.layout.simple_dropdown_item_1line, listaCategoriaTienda);
            spCategoriaTienda.setAdapter(ct);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void llenarSpinnnerCiudad(){
        final String ip1 = getString(R.string.ip);
        String url = ip1 + "/findyourstyleBDR/wsJSONCiudad.php";
        categoriaTienda.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    cargarSpinnerCiudad(new String(responseBody));
                }else{

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void cargarSpinnerCiudad(String respuesta){
        ArrayList<Ciudad> listaCiudad = new ArrayList<Ciudad>();
        try {
            JSONObject jsonObj = new JSONObject(respuesta);
            JSONArray jsonArreglo =  jsonObj.optJSONArray("ciudad");
            for (int i = 0; i < jsonArreglo.length(); i++){
                Ciudad c = new Ciudad();
                c.setNombreCiudad(jsonArreglo.getJSONObject(i).optString("nombre_ciudad"));
                listaCiudad.add(c);
            }
            ArrayAdapter<Ciudad> ct = new ArrayAdapter<Ciudad>(RegistroTiendaActivity.this,android.R.layout.simple_dropdown_item_1line, listaCiudad);
            spCiudadTienda.setAdapter(ct);
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

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
