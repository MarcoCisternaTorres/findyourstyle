package com.example.findyourstyle.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Activities.InicioSesionTienda;
import com.example.findyourstyle.Modelo.CategoriaServicio;
import com.example.findyourstyle.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.getIntent;


public class AgregarProductoFragment extends Fragment {


        String correoTienda;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
                Bundle bundle = getArguments();
                correoTienda = bundle.getString("correoTienda", "No hay correo");
        }

        private EditText nombreProducto, precioProducto;
        private ImageView imgProducto;
        private ImageView btnImagen;
        private Button btnRegistrarProducto;
        private Spinner spinnerNuevoProducto;
        private AsyncHttpClient asyncHttpClient;
        private TextView correoIdTienda;
        private StringRequest stringRequest;
        private ProgressDialog progreso;
        private Bitmap bitmap;
        RequestQueue request;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_agregar_producto, container, false);

            imgProducto = view.findViewById(R.id.imgNuevoProducto);
            btnImagen = view.findViewById(R.id.btnAgregarNuevoProducto);
            spinnerNuevoProducto = view.findViewById(R.id.spNuevoProducto);
            nombreProducto = view.findViewById(R.id.etxtNuevoProducto);
            precioProducto = view.findViewById(R.id.etxtPrecioNuevoProducto);
            btnRegistrarProducto = view.findViewById(R.id.btnRegistroNuevoProducto);
            correoIdTienda = view.findViewById(R.id.txtIdCorreoTienda);

            request = Volley.newRequestQueue(getContext());
            asyncHttpClient = new AsyncHttpClient();
            llenarSpinnner();

            btnImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargarImagen();
                }
            });
            btnRegistrarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cargarWebService();
                }
            });

            return view;
        }





        private void cargarImagen() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            startActivityForResult(intent.createChooser(intent, "Seleccione una Apliacación"), 10);

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10) {
                Uri path = data.getData();
                imgProducto.setImageURI(path);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                    imgProducto.setImageBitmap(bitmap);
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

        private void llenarSpinnner() {
            final String ip1 = getString(R.string.ip);
            String url = ip1 + "/findyourstyleBDR/wsJSONCategoriaServicio.php";
            asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        cargarSpinner(new String(responseBody));
                    } else {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

        private void cargarSpinner(String respuesta) {
            ArrayList<CategoriaServicio> listaCategoriaServicio = new ArrayList<CategoriaServicio>();
            try {
                JSONObject jsonObj = new JSONObject(respuesta);
                JSONArray jsonArreglo = jsonObj.optJSONArray("categoria_servicio");
                for (int i = 0; i < jsonArreglo.length(); i++) {
                    CategoriaServicio cs = new CategoriaServicio();
                    cs.setNombreCategoriaServicio(jsonArreglo.getJSONObject(i).optString("nombre_servicio"));
                    listaCategoriaServicio.add(cs);
                }
                ArrayAdapter<CategoriaServicio> c = new ArrayAdapter<CategoriaServicio>(getContext(), android.R.layout.simple_dropdown_item_1line, listaCategoriaServicio);
                spinnerNuevoProducto.setAdapter(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        private void cargarWebService() {
            //Barra de progreso
            progreso = new ProgressDialog(getContext());
            progreso.setMessage("Cargando...");
            progreso.show();
            // Enviar datos al web service
            final String ip = getString(R.string.ip);
            String url = ip + "/findyourstyleBDR/wsJSONRegistroProducto.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getContext(),"Producto registrado exitosamente", Toast.LENGTH_SHORT).show();
                    progreso.hide();
                    nombreProducto.setText("");
                    precioProducto.setText("");
                    imgProducto.setImageResource(R.drawable.ic_launcher_background);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),"Producto no se ha registrado", Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String nombreP = nombreProducto.getText().toString();
                    String precioP = precioProducto.getText().toString();
                    String imagenP = convertirImagenString(bitmap);
                    //String imagenP = String.valueOf(imgProducto);
                    String categoriaP = spinnerNuevoProducto.getSelectedItem().toString();



                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("nombre_producto", nombreP);
                    parametros.put("precio", precioP);
                    parametros.put("imagen",imagenP );
                    parametros.put("nombre_categoria_servicio", categoriaP);
                    parametros.put("correo_tienda", correoTienda);

                    return parametros;
                }
            };
            request.add(stringRequest);


        }

        private String convertirImagenString(Bitmap bitmap){
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
            byte [] imageByte = array.toByteArray();
            String imagenString = Base64.encodeToString(imageByte,Base64.DEFAULT);


            return imagenString;
        }

    }