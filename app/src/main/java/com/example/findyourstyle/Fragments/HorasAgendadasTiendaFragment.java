package com.example.findyourstyle.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Adampters.AdapterHorasTienda;
import com.example.findyourstyle.Modelo.HorasAtencionTienda;
import com.example.findyourstyle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HorasAgendadasTiendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorasAgendadasTiendaFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HorasAgendadasTiendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HorasAgendadasTiendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HorasAgendadasTiendaFragment newInstance(String param1, String param2) {
        HorasAgendadasTiendaFragment fragment = new HorasAgendadasTiendaFragment();
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
    RecyclerView recyclerHorasagendadasTienda;
    ArrayList<HorasAtencionTienda> listaHoraAtencionTienda;
    private StringRequest stringRequest;
    RequestQueue request;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_horas_agendadas_tienda, container, false);
        recyclerHorasagendadasTienda =  view.findViewById(R.id.recyclerViewHorasAtencionTienda);
        recyclerHorasagendadasTienda.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerHorasagendadasTienda.setHasFixedSize(true);
        recyclerHorasagendadasTienda.setLayoutManager(new GridLayoutManager(getContext(), 1));

        listaHoraAtencionTienda = new ArrayList<>();
        request = Volley.newRequestQueue(getContext());
        buscarHorasAtencion();

        return  view;
    }

    public  void buscarHorasAtencion(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultarHorasAgendadasTienda.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HorasAtencionTienda horasAtencionTienda = null;
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("reservar_hora");
                    for (int i = 0; i < jsonArreglo.length(); i++) {
                        horasAtencionTienda = new HorasAtencionTienda();
                        horasAtencionTienda.setNombreProducto(jsonArreglo.getJSONObject(i).optString("nombre_producto"));
                        horasAtencionTienda.setNombreCliente(jsonArreglo.getJSONObject(i).optString("nombre_usuario"));
                        horasAtencionTienda.setDiaAtencion(jsonArreglo.getJSONObject(i).optString("dia_atencion"));
                        horasAtencionTienda.setHoraAtencion(jsonArreglo.getJSONObject(i).optString("hora_atencion"));
                        horasAtencionTienda.setRutaImagen(jsonArreglo.getJSONObject(i).optString("ruta_imagen"));
                        listaHoraAtencionTienda.add(horasAtencionTienda);
                    }
                    AdapterHorasTienda adapterHorasTienda = new AdapterHorasTienda(listaHoraAtencionTienda,getContext());
                    recyclerHorasagendadasTienda.setAdapter(adapterHorasTienda);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No ha agendado una hora de atención", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correoT = correoTienda;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
