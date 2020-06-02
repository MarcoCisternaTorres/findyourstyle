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
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Activities.HomeActivity;
import com.example.findyourstyle.Interfaces.IComunicaFragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

    AdapterBuscar adapterBuscar;
    RecyclerView recyclerViewBuscar;
    ArrayList<ModeloBuscar> productos;

    ProgressDialog progressDialog;

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
        request = Volley.newRequestQueue(getContext());

        productos    = new ArrayList<>();
        btnAtras = view.findViewById(R.id.fechaAtras_fragmentBuscar);
        //cargarProductos();
        //mostarData();

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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Buscando productos");
        progressDialog.show();
        String url = "http://192.168.1.57/findyourstyleBDR/consultaListaProductos.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

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

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
        System.out.println();

        progressDialog.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        ModeloBuscar modeloBuscar=null;

        JSONArray json=response.optJSONArray("producto");

        try {

            for (int i=0; i<json.length(); i++){
                modeloBuscar = new ModeloBuscar();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                modeloBuscar.setNombreProducto(jsonObject.optString("nombre_producto"));
                modeloBuscar.setPrecio(jsonObject.optInt("precio"));
                modeloBuscar.setTienda(jsonObject.optString("nombre_tienda"));
                modeloBuscar.setDireccion(jsonObject.optString("direccion_tienda"));

                //modeloBuscar.setIdImagenBuscar(jsonObject.optInt(String.valueOf(R.drawable.ic_launcher_background)));
                productos.add(modeloBuscar);
            }
            progressDialog.hide();
            AdapterBuscar adapter=new AdapterBuscar(getContext(),productos);
            recyclerViewBuscar.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            progressDialog.hide();
        }

    }
}
