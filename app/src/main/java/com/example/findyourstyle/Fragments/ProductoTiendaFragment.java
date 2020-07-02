package com.example.findyourstyle.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Adampters.AdapterListaProducto;
import com.example.findyourstyle.Adampters.AdapterProductosTienda;
import com.example.findyourstyle.Interfaces.IComunicaFragment;
import com.example.findyourstyle.Interfaces.IDetalleFragment;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.Modelo.ProductoTienda;
import com.example.findyourstyle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductoTiendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductoTiendaFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductoTiendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductoTiendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductoTiendaFragment newInstance(String param1, String param2) {
        ProductoTiendaFragment fragment = new ProductoTiendaFragment();
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
        correoTienda = bundle.getString("correoTienda", "No hay correo");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerProductosTiendas;
    ArrayList<ProductoTienda> lista;

    ProgressDialog progress;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    Activity actividad;
    IComunicaFragment iComunicaFragment;

    ImageView imgAgregarHoras;
    Fragment horas;
    Fragment detalleHoras;

    Activity activity;
    IDetalleFragment iDetalleFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_producto_tienda, container, false);

        recyclerProductosTiendas =  view.findViewById(R.id.recyclerProductosTiendas);
        recyclerProductosTiendas.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerProductosTiendas.setHasFixedSize(true);
        recyclerProductosTiendas.setLayoutManager(new GridLayoutManager(getContext(), 1));

        horas = new Horas();
        detalleHoras = new DetalleFragment();

        lista = new ArrayList<>();
        request = Volley.newRequestQueue(getContext());
        cargarWebService();

        final Bundle bundle = new Bundle();
        bundle.putString("correoTienda",correoTienda);
        detalleHoras.setArguments(bundle);



        return  view;
    }

    private void cargarWebService(){
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando");
        progress.show();

        final String ip = getString(R.string.ip);

        String url = ip + "/findyourstyleBDR/consultarProductosTienda.php?correo_tienda="+correoTienda;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {


    }

    @Override
    public void onResponse(JSONObject response) {
        ProductoTienda productoTienda = null;
        JSONArray jsonArray =  response.optJSONArray("producto");
        try{
            for(int i = 0; i < jsonArray.length();i++){
                productoTienda = new ProductoTienda();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);

                productoTienda.setNombreProducto(jsonObject.optString("nombre_producto"));
                productoTienda.setTienda(jsonObject.optString("nombre_tienda"));
                productoTienda.setPrecio(jsonObject.optString("precio"));
                productoTienda.setRutaImagen(jsonObject.optString("ruta_imagen"));
                productoTienda.setDireccion(jsonObject.optString("direccion_tienda"));
                lista.add(productoTienda);
            }
            progress.hide();

            AdapterProductosTienda adapterProductosTienda = new AdapterProductosTienda(lista,getContext());
            recyclerProductosTiendas.setAdapter(adapterProductosTienda);
            adapterProductosTienda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iDetalleFragment.enviarDetalle(lista.get(recyclerProductosTiendas.getChildAdapterPosition(v)));
                }
            });

        }catch (JSONException e){
            e.printStackTrace();

            progress.hide();

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof  Activity){
            this.actividad = (Activity) context;
            iDetalleFragment = (IDetalleFragment) this.actividad;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
