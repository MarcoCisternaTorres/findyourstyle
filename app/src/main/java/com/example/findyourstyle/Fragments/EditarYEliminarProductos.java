package com.example.findyourstyle.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.CategoriaServicio;
import com.example.findyourstyle.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarYEliminarProductos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarYEliminarProductos extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public EditarYEliminarProductos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarYEliminarProductos.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarYEliminarProductos newInstance(String param1, String param2) {
        EditarYEliminarProductos fragment = new EditarYEliminarProductos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String nombreProducto;
    String correoTienda;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        nombreProducto = bundle.getString("nombreProducto", "No hay nombre");
        correoTienda = bundle.getString("correoTienda", "No hay correo");
    }

    EditText etxtNombreProducto, etxtPrecio;
    Spinner spProducto;
    ImageView imgProducto, imgTraerProducto;
    ProgressDialog progreso;
    private StringRequest stringRequest;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private AsyncHttpClient asyncHttpClient;
    private Bitmap bitmap;
    Button btnEditar, btnEliminar;
    AlertDialog.Builder alert;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_y_eliminar_productos, container, false);
        etxtNombreProducto = view.findViewById(R.id.etxtEditarProducto);
        etxtPrecio = view.findViewById(R.id.etxtEditarPrecioProducto);
        spProducto = view.findViewById(R.id.spEditarProducto);
        imgProducto = view.findViewById(R.id.imgEditarP);
        imgTraerProducto = view.findViewById(R.id.imgEditarProducto);
        btnEditar = view.findViewById(R.id.btnUpdateProducto);
        btnEliminar = view.findViewById(R.id.btnDeleteProducto);
        asyncHttpClient = new AsyncHttpClient();
        request = Volley.newRequestQueue(getContext());
        alert = new AlertDialog.Builder(getContext());

        btnEditar.setOnClickListener(this);
        imgTraerProducto.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        llenarSpinnner();
        cargarWebService();

        return view;
    }
    @Override
    public void onClick(View v) {
        if(v == btnEditar){

            alert.setMessage("¿Desea Editar Los Cambios?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editarProducto();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });AlertDialog titulo = alert.create();
            titulo.setTitle("Alerta");
            titulo.show();
        }
        if(v == imgTraerProducto){
            cargarImagen();
        }
        if(v == btnEliminar){
            eliminarProducto();
        }
    }
    private void cargarWebService() {
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultarProductoEditar.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<CategoriaServicio> listaCategoriaServicio = new ArrayList<CategoriaServicio>();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("producto");
                    for (int i = 0; i < jsonArreglo.length(); i++) {
                        etxtNombreProducto.setText(jsonArreglo.getJSONObject(i).optString("nombre_producto").toString());
                        etxtPrecio.setText(jsonArreglo.getJSONObject(i).optString("precio").toString());

                        cargarImagenServidor(jsonArreglo.getJSONObject(i).optString("ruta_imagen"));

                        String inicializarItem = jsonArreglo.getJSONObject(i).optString("nombre_servicio");
                        spProducto.setSelection(obtenerPosicionItem(spProducto, inicializarItem));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                String nombreP = nombreProducto;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoTienda);
                parametros.put("nombre_producto", nombreP);


                return parametros;
            }
        };
        request.add(stringRequest);


    }

    private void cargarImagenServidor(String rutaImagen){
        String ip=getActivity().getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgProducto.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
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
            spProducto.setAdapter(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int obtenerPosicionItem(Spinner spinner, String categoria) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(categoria)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }

    public  void editarProducto(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/editarProducto.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Producto editado exitosamente", Toast.LENGTH_SHORT).show();
                etxtNombreProducto.setText("");
                etxtPrecio.setText("");
                //imgProducto.setImageResource(R.drawable.ic_launcher_background);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Producto no se ha editado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nombreP = nombreProducto;
                String correo = correoTienda;
                String nuevoNombre = etxtNombreProducto.getText().toString();
                String precio = etxtPrecio.getText().toString();
                String categoriaP = spProducto.getSelectedItem().toString();
                String imagenP = convertirImagenString(bitmap);

                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre_antiguo", nombreP);
                parametros.put("correo_tienda", correo);
                parametros.put("nuevo_nombre_producto", nuevoNombre);
                parametros.put("precio", precio);
                parametros.put("nombre_categoria", categoriaP);
                parametros.put("imagen", imagenP);




                return parametros;
            }
        };
        request.add(stringRequest);
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
    private String convertirImagenString(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte [] imageByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imageByte,Base64.DEFAULT);


        return imagenString;
    }

    public  void eliminarProducto(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/eliminarProducto.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                etxtNombreProducto.setText("");
                etxtPrecio.setText("");
                //imgProducto.setImageResource(R.drawable.ic_launcher_background);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Producto no se ha eliminado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correo = correoTienda;
                String nombreProducto = etxtNombreProducto.getText().toString();
                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correo);
                parametros.put("nombre_producto", nombreProducto);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

}