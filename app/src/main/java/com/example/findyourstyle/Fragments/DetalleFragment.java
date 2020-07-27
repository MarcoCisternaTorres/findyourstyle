package com.example.findyourstyle.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Adampters.AdapterAgendaUsuario;
import com.example.findyourstyle.Adampters.AdapterHorasDetalle;
import com.example.findyourstyle.Adampters.AdapterProductosTienda;
import com.example.findyourstyle.FragmentsCrudProducto.EditarCategoriaFragment;
import com.example.findyourstyle.FragmentsCrudProducto.EditarNombreProductoFragment;
import com.example.findyourstyle.FragmentsCrudProducto.EditarPrecioFragment;
import com.example.findyourstyle.Modelo.HorasTiendaDetalle;
import com.example.findyourstyle.Modelo.HorasUsuario;
import com.example.findyourstyle.Modelo.ProductoTienda;
import com.example.findyourstyle.R;
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
 * Use the {@link DetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleFragment extends Fragment implements  View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleFragment newInstance(String param1, String param2) {
        DetalleFragment fragment = new DetalleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String correoTienda;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            correoTienda = bundle.getString("correoTienda", "No hay correo");
        }


    }


    private TextView nombreProducto, nombreTienda, direccion, precio, categoriaProducto;
    private ImageView imgDetalle, imgHora, imgEditar, imgVolverAtras, imgEditarImagenProducto, imgEliminarProducto;
    Fragment horas, editar, editarNombreProducto, editarPecio, editarCategoria;
    RequestQueue request;
    private StringRequest stringRequest;
    RecyclerView recyclerHorasProductos;
    ArrayList<HorasTiendaDetalle> listaHorasDetalle;
    ProductoTiendaFragment productoTiendaFragment;
    private Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle, container, false);
        recyclerHorasProductos = view.findViewById(R.id.recyclerViewDetalleHora);
        recyclerHorasProductos.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerHorasProductos.setHasFixedSize(true);
        recyclerHorasProductos.setLayoutManager(new GridLayoutManager(getContext(), 1));
        listaHorasDetalle = new ArrayList<>();
        productoTiendaFragment = new ProductoTiendaFragment();

        nombreProducto = view.findViewById(R.id.txtNombreProductoDetalle);
        precio = view.findViewById(R.id.txtPrecioDetalle);
        categoriaProducto = view.findViewById(R.id.txtCategoriaDetalle);
        imgDetalle = view.findViewById(R.id.imgDetalle);
        imgEditarImagenProducto = view.findViewById(R.id.imgEditarImagenProducto);
        imgHora = view.findViewById(R.id.imgAgregarHoraDetalle);
        //imgEditar = view.findViewById(R.id.imgEditarproducto);
        imgEditar = view.findViewById(R.id.iconVolverAtrasDetalle);
        imgEliminarProducto = view.findViewById(R.id.imgEliminarProducto);

        request = Volley.newRequestQueue(getContext());

        horas = new Horas();
        editar = new EditarYEliminarProductos();
        editarNombreProducto = new EditarNombreProductoFragment();
        editarPecio = new  EditarPrecioFragment();
        editarCategoria = new EditarCategoriaFragment();

        final Bundle bundle = new Bundle();
        bundle.putString("correoTienda",correoTienda);
        horas.setArguments(bundle);

        consultarHoraAtencionProductos();

        final Bundle bundleProducatodTienda = new Bundle();
        bundleProducatodTienda.putString("correoTienda",correoTienda);
        productoTiendaFragment.setArguments(bundleProducatodTienda);

        Bundle productos = new Bundle(getArguments());
        ProductoTienda productosTienda = null;
        if (productos != null){
            productosTienda = (ProductoTienda) productos.getSerializable("objeto");
            nombreProducto.setText(productosTienda.getNombreProducto());
            precio.setText(productosTienda.getPrecio());


            if(productosTienda.getRutaImagen()!=null){
                cargarImagenServidor(productosTienda.getRutaImagen());
            }else{
                imgDetalle.setImageResource(R.drawable.ic_launcher_background);
            }

            final Bundle bundleNombreHoras = new Bundle();
            bundleNombreHoras.putString("nombreProducto",productosTienda.getNombreProducto());
            bundleNombreHoras.putString("correoTienda",correoTienda);
            horas.setArguments(bundleNombreHoras);

            final Bundle bundleEditarProducto= new Bundle();
            bundleEditarProducto.putString("nombreProducto",productosTienda.getNombreProducto());
            bundleEditarProducto.putString("correoTienda",correoTienda);
            editar.setArguments(bundleEditarProducto);
        }
        conusultarCategoriaProducto();
        final Bundle bundleEditarNombreP = new Bundle();
        bundleEditarNombreP.putString("correoTienda",correoTienda);
        bundleEditarNombreP.putString("nombreProducto",nombreProducto.getText().toString());
        bundleEditarNombreP.putString("precioProducto",precio.getText().toString());
        editarNombreProducto.setArguments(bundleEditarNombreP);


        final Bundle bundleEditarPercioP = new Bundle();
        bundleEditarPercioP.putString("correoTienda",correoTienda);
        bundleEditarPercioP.putString("nombreProducto",nombreProducto.getText().toString());
        bundleEditarPercioP.putString("precioProducto",precio.getText().toString());
        editarPecio.setArguments(bundleEditarPercioP);




        imgHora.setOnClickListener(this);
//        imgEditar.setOnClickListener(this);
        imgEditar.setOnClickListener(this);
        nombreProducto.setOnClickListener(this);
        precio.setOnClickListener(this);
        categoriaProducto.setOnClickListener(this);
        imgEditarImagenProducto.setOnClickListener(this);
        imgEliminarProducto.setOnClickListener(this);

        return view;
    }
    private void cargarImagenServidor(String rutaImagen){
        String ip=getActivity().getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgDetalle.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == imgHora){
            setFragment(horas);
        }
        /*if(v == imgEditar){
            setFragment(editar);
        }*/
        if(v == imgEditar){
            setFragment(productoTiendaFragment);
        }
        if(v == nombreProducto){
            setFragment(editarNombreProducto);
        }
        if (v == precio){
            setFragment(editarPecio);
        }
        if (v == categoriaProducto){
            setFragment(editarCategoria);
        }
        if (v == imgEditarImagenProducto){
            cargarImagen();
        }
        if(v == imgEliminarProducto){
            eliminarProducto();
        }
    }

    public  void consultarHoraAtencionProductos(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultarHorasPorProductos.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                        try {
                            HorasTiendaDetalle horasTiendaDetalle = null;
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray jsonArreglo = jsonObj.optJSONArray("hora_atencion");
                            for (int i = 0; i < jsonArreglo.length(); i++) {
                                horasTiendaDetalle = new HorasTiendaDetalle();
                                horasTiendaDetalle.setFecha_atencion(jsonArreglo.getJSONObject(i).optString("dia_atencion"));
                                horasTiendaDetalle.setHora_atencion(jsonArreglo.getJSONObject(i).optString("hora_atencion"));
                                listaHorasDetalle.add(horasTiendaDetalle);
                            }
                            AdapterHorasDetalle adapterHorasDetalle = new AdapterHorasDetalle(listaHorasDetalle, getContext());
                            recyclerHorasProductos.setAdapter(adapterHorasDetalle);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "No ha registrado ninguna hora de atención", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String correoT = correoTienda;
                    String nombreP = nombreProducto.getText().toString();

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("correo_tienda", correoT);
                    parametros.put("nombre_producto", nombreP);
                    return parametros;
                }
            };
            request.add(stringRequest);
    }
    public  void conusultarCategoriaProducto(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaProductoTienda/consultarCategoriaDetalle.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("categoria_servicio");
                    for (int i = 0; i < jsonArreglo.length(); i++) {
                        categoriaProducto.setText(jsonArreglo.getJSONObject(i).optString("nombre_servicio"));
                    }

                    final Bundle bundleEditarCategoria = new Bundle();
                    bundleEditarCategoria.putString("correoTienda",correoTienda);
                    bundleEditarCategoria.putString("nombrProducto",nombreProducto.getText().toString());
                    bundleEditarCategoria.putString("categoriaProducto",categoriaProducto.getText().toString());
                    editarCategoria.setArguments(bundleEditarCategoria);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No ha conectado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correoT = correoTienda;
                String nombreP = nombreProducto.getText().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
                parametros.put("nombre_producto", nombreP);
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
            imgDetalle.setImageURI(path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                imgDetalle.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap=redimensionarImagen(bitmap,800,800);
        }
        editarImagen();
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }

    }

    public  void editarImagen(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/editarImagenProduto.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Imagen editada exitosamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"La imagen no se ha editado", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correoT = correoTienda;
                String nombreP = nombreProducto.getText().toString();
                String imagenT = convertirImagenString(bitmap);


                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
                parametros.put("nombre_producto", nombreP);
                parametros.put("imagen", imagenT);
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

    public  void eliminarProducto(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaProductoTienda/eliminarProducto.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                            Toast.makeText(getContext(), "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                            setFragment(productoTiendaFragment);

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
                String nombreP = nombreProducto.getText().toString();
                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correo);
                parametros.put("nombre_producto", nombreP);
                return parametros;
            }
        };
        request.add(stringRequest);
    }


    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragmentTienda, fragment);
        fragmentTransaction.commit();
    }
}
