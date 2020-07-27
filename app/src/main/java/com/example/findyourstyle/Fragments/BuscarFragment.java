package com.example.findyourstyle.Fragments;


import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Activities.HomeActivity;
import com.example.findyourstyle.Adampters.AdapterListaProducto;
import com.example.findyourstyle.Interfaces.IComunicaFragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.findyourstyle.Adampters.AdapterBuscar;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarFragment extends Fragment {


    String correoUsuario;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        correoUsuario = bundle.getString("correoUsuario", "No hay correo");

    }

    RecyclerView recyclerViewBuscar;
    ArrayList<ModeloBuscar> listaProductos;
    ArrayList<ModeloBuscar> buscarProductosLista;
    ArrayList<ModeloBuscar> setProductosLista;

    ProgressDialog progress;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    EditText etxtBuscar;
    ImageView btnAtras, imgBuscar, imgBorrarCampos;


    //comunicacion de fragments
    Activity actividad;
    IComunicaFragment iComunicaFragment;
    private StringRequest stringRequest;


    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view    = inflater.inflate(R.layout.fragment_buscar, container, false);
        recyclerViewBuscar =  view.findViewById(R.id.recycler_view_buscar);
        recyclerViewBuscar.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewBuscar.setHasFixedSize(true);
        recyclerViewBuscar.setLayoutManager(new GridLayoutManager(getContext(), 1));
        etxtBuscar = view.findViewById(R.id.etxtBuscarProducto);
        imgBuscar = view.findViewById(R.id.imgBuscar);
        imgBorrarCampos = view.findViewById(R.id.imgBorrarCampoBuscar);
        listaProductos = new ArrayList<>();
        buscarProductosLista = new ArrayList<>();
        setProductosLista = new ArrayList<>();
        request = Volley.newRequestQueue(getContext());

       // btnAtras = view.findViewById(R.id.fechaAtras_fragmentBuscar);

        cargarWebService();
        imgBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etxtBuscar.getText().toString().isEmpty()){
                    buscarProducto();
                }else{
                    Toast.makeText(getContext(), "Debe ingresar un nombre", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgBorrarCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPrdoctos();
                etxtBuscar.setText("");
            }
        });

        return view;
    }



    private void buscarProducto() {
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/buscarProductoNombre.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ModeloBuscar modeloBuscar = null;

                try{
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray =  jsonObj.optJSONArray("producto");
                    for(int i = 0; i < jsonArray.length();i++){
                        modeloBuscar = new ModeloBuscar();
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);

                        modeloBuscar.setNombreProducto(jsonObject.optString("nombre_producto"));
                        modeloBuscar.setTienda(jsonObject.optString("nombre_tienda"));
                        modeloBuscar.setPrecio(jsonObject.optString("precio"));
                        modeloBuscar.setRutaImagen(jsonObject.optString("ruta_imagen"));
                        modeloBuscar.setDireccion(jsonObject.optString("direccion_tienda"));
                        listaProductos.add(modeloBuscar);
                    }


                    AdapterBuscar adapterBuscar = new AdapterBuscar(listaProductos,actividad);
                    recyclerViewBuscar.setAdapter(adapterBuscar);
                    adapterBuscar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            iComunicaFragment.enviarProducto(listaProductos.get(recyclerViewBuscar.getChildAdapterPosition(v)));
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Producto no encontrado", Toast.LENGTH_LONG).show();
                    progress.hide();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Producto no se ha registrado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nombreP = etxtBuscar.getText().toString();




                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre_producto", nombreP);
                return parametros;
            }
        };
        request.add(stringRequest);


    }

    private void setPrdoctos() {
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaListaProductos.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ModeloBuscar modeloBuscar = null;

                try{
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray =  jsonObj.optJSONArray("producto");
                    for(int i = 0; i < jsonArray.length();i++){
                        modeloBuscar = new ModeloBuscar();
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);

                        modeloBuscar.setNombreProducto(jsonObject.optString("nombre_producto"));
                        modeloBuscar.setTienda(jsonObject.optString("nombre_tienda"));
                        modeloBuscar.setPrecio(jsonObject.optString("precio"));
                        modeloBuscar.setRutaImagen(jsonObject.optString("ruta_imagen"));
                        modeloBuscar.setDireccion(jsonObject.optString("direccion_tienda"));
                        setProductosLista.add(modeloBuscar);
                    }


                    AdapterBuscar adapterBuscar = new AdapterBuscar(setProductosLista,actividad);
                    recyclerViewBuscar.setAdapter(adapterBuscar);
                    adapterBuscar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            iComunicaFragment.enviarProducto(setProductosLista.get(recyclerViewBuscar.getChildAdapterPosition(v)));
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"no se ha podido conectar con el servidor"+""+response, Toast.LENGTH_LONG).show();
                    progress.hide();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Producto no se ha registrado", Toast.LENGTH_SHORT).show();

            }
        });
        request.add(stringRequest);


    }

    private void cargarWebService() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando");


        final String ip = getString(R.string.ip);

        String url = ip + "/findyourstyleBDR/consultaListaProductos.php";



        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ModeloBuscar modeloBuscar = null;

                try{
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray =  jsonObj.optJSONArray("producto");
                    for(int i = 0; i < jsonArray.length();i++){
                        modeloBuscar = new ModeloBuscar();
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);

                        modeloBuscar.setNombreProducto(jsonObject.optString("nombre_producto"));
                        modeloBuscar.setTienda(jsonObject.optString("nombre_tienda"));
                        modeloBuscar.setPrecio(jsonObject.optString("precio"));
                        modeloBuscar.setRutaImagen(jsonObject.optString("ruta_imagen"));
                        modeloBuscar.setDireccion(jsonObject.optString("direccion_tienda"));
                        buscarProductosLista.add(modeloBuscar);
                    }


                    AdapterBuscar adapterBuscar = new AdapterBuscar(buscarProductosLista,actividad);
                    recyclerViewBuscar.setAdapter(adapterBuscar);
                    adapterBuscar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            iComunicaFragment.enviarProducto(buscarProductosLista.get(recyclerViewBuscar.getChildAdapterPosition(v)));
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Producto no encontrado", Toast.LENGTH_LONG).show();
                    progress.hide();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(actividad, "No existen productos disponibles", Toast.LENGTH_LONG).show();
                System.out.println();
                progress.hide();
                Log.d("ERROR: ", error.toString());

            }
        });
        request.add(stringRequest);


    }


    // comunicacion de fragment a fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.actividad = (Activity) context;
            iComunicaFragment = (IComunicaFragment) this.actividad;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
