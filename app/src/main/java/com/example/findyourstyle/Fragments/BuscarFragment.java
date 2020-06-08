package com.example.findyourstyle.Fragments;


import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    RecyclerView recyclerViewBuscar;
    ArrayList<ModeloBuscar> listaProductos;

    ProgressDialog progress;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    ImageView btnAtras;


    //comunicacion de fragments
    Activity actividad;
    IComunicaFragment iComunicaFragment;


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

        listaProductos = new ArrayList<>();
        request = Volley.newRequestQueue(getContext());
        btnAtras = view.findViewById(R.id.fechaAtras_fragmentBuscar);

        cargarWebService();

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InicioFragment inicioFragment = new InicioFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorFragment, inicioFragment);
                transaction.commit();
            }
        });
        return view;
    }

    private void cargarWebService() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando");
        progress.show();

        final String ip = getString(R.string.ip);

        String url = ip + "/findyourstyleBDR/consultaListaProductos.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(actividad, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        progress.hide();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        ModeloBuscar modeloBuscar = null;
        JSONArray jsonArray =  response.optJSONArray("producto");
        try{
            for(int i = 0; i < jsonArray.length();i++){
                modeloBuscar = new ModeloBuscar();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);

                modeloBuscar.setNombreProducto(jsonObject.optString("nombre_producto"));
                modeloBuscar.setTienda(jsonObject.optString("nombre_tienda"));
                modeloBuscar.setPrecio(jsonObject.optString("precio"));
                modeloBuscar.setDireccion(jsonObject.optString("direccion_tienda"));
                listaProductos.add(modeloBuscar);
            }
            progress.hide();
            AdapterListaProducto adapterListaProducto = new AdapterListaProducto(listaProductos);
            recyclerViewBuscar.setAdapter(adapterListaProducto);
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getContext(),"no se ha podido conectar con el servidor"+""+response, Toast.LENGTH_LONG).show();
            progress.hide();

        }
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
