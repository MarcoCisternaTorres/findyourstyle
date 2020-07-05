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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Adampters.AdapterAgendaUsuario;
import com.example.findyourstyle.Adampters.AdapterHorasUsuario;
import com.example.findyourstyle.Modelo.HorasAtencion;
import com.example.findyourstyle.Modelo.HorasUsuario;
import com.example.findyourstyle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {


    public AgendaFragment() {
        // Required empty public constructor
    }

    String correoUsuario;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        correoUsuario = bundle.getString("correoUsuario", "No hay correo");
    }

    private StringRequest stringRequest;
    ArrayList<HorasUsuario> listaHorasUsuario;
    RecyclerView recyclerconsultarHora;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        recyclerconsultarHora = view.findViewById(R.id.recyclerViewAgenda);
        recyclerconsultarHora.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerconsultarHora.setHasFixedSize(true);
        recyclerconsultarHora.setLayoutManager(new GridLayoutManager(getContext(), 1));

        listaHorasUsuario = new ArrayList<>();
        request = Volley.newRequestQueue(getContext());
        consultarHoraAtencion();

        return view;
    }

    public  void consultarHoraAtencion(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultarAgendaUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HorasUsuario horasUsuario = null;
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("reservar_hora");
                    for (int i = 0; i < jsonArreglo.length(); i++) {
                        horasUsuario = new HorasUsuario();
                        horasUsuario.setFecha_atencion(jsonArreglo.getJSONObject(i).optString("dia_atencion"));
                        horasUsuario.setHora_atencion(jsonArreglo.getJSONObject(i).optString("hora_atencion"));
                        listaHorasUsuario.add(horasUsuario);
                    }
                    AdapterAgendaUsuario adapterAgendaUsuario = new AdapterAgendaUsuario(listaHorasUsuario,getContext());
                    recyclerconsultarHora.setAdapter(adapterAgendaUsuario);

                    adapterAgendaUsuario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

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
                String correoU = correoUsuario;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_usuario", correoU);
                return parametros;
            }
        };
        request.add(stringRequest);

    }

}
