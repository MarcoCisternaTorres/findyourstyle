package com.example.findyourstyle.Activities;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.CategoriaTienda;
import com.example.findyourstyle.R;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.*;

import java.util.ArrayList;

public class RegistroTiendaActivity extends AppCompatActivity implements  Response.Listener<JSONObject>,Response.ErrorListener{

    private EditText nombreTienda;
    private EditText direccionTienda;
    private EditText correoTienda;
    private EditText contraseniaTienda;
    private EditText getContraseniaTienda2;

    private Button btnRegistrarTienda;

    // Pantalla de progreso
    ProgressDialog progreso;

    //Conexion con el web service
    RequestQueue request, requestSpinner;
    JsonObjectRequest jsonObjectRequest, jsonObjectRequestSpninner;

    //Conexion con el web service para el spinner
    private AsyncHttpClient categoriaTienda;
    Spinner spCategoriaTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tienda);

        nombreTienda = findViewById(R.id.etxtNombreTiendaRegistro);
        direccionTienda = findViewById(R.id.txtDireccionTienda);
        correoTienda = findViewById(R.id.etxtCorreoRegistroTienda);
        contraseniaTienda = findViewById(R.id.etxtContraseniaRegistroTienda);
        getContraseniaTienda2 = findViewById(R.id.etxtContrasenia2RegistroTienda);

        btnRegistrarTienda = findViewById(R.id.btnRegistrarTienda);

        request = Volley.newRequestQueue(this);

        categoriaTienda = new AsyncHttpClient();
        spCategoriaTienda = findViewById(R.id.spinnerCetgoriaTienda);

        llenarSpinnner();

        btnRegistrarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contraseniaTienda.getText().toString().equals(contraseniaTienda.getText().toString())){
                    cargarWebService();
                }else {
                    Toast.makeText(RegistroTiendaActivity.this,"Las contraseñas no son igual", Toast.LENGTH_SHORT).show();
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
        String url = ip +"/findyourstyleBDR/wsJSONRegistroTienda.php?nombre_tienda="+nombreTienda.getText().toString()+
                "&correo_tienda="+correoTienda.getText().toString()+"&contrasenia_tienda="+contraseniaTienda.getText().toString()+"&direccion_tienda="+direccionTienda.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this,"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        progreso.hide();
        nombreTienda.setText("");
        direccionTienda.setText("");
        correoTienda.setText("");
        contraseniaTienda.setText("");
        getContraseniaTienda2.setText("");
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

}
