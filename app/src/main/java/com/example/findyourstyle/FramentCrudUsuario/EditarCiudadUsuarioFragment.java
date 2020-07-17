package com.example.findyourstyle.FramentCrudUsuario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.CategoriaServicio;
import com.example.findyourstyle.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarCiudadUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarCiudadUsuarioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarCiudadUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarCiudadUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarCiudadUsuarioFragment newInstance(String param1, String param2) {
        EditarCiudadUsuarioFragment fragment = new EditarCiudadUsuarioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String correoUsuario;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle = getArguments();
        correoUsuario = bundle.getString("correoUsuario", "No hay correo");
    }
    Spinner spCiudadUSuario;
    Button btnEditarCiudad;
    RequestQueue request;
    private StringRequest stringRequest;
    private AsyncHttpClient asyncHttpClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_ciudad_usuario, container, false);
        spCiudadUSuario = view.findViewById(R.id.spCiudadUsuario);
        btnEditarCiudad = view.findViewById(R.id.btnEditarCiudadUsuario);
        request = Volley.newRequestQueue(getContext());
        asyncHttpClient = new AsyncHttpClient();
        llenarSpinnner();
        conusultarCiudadUsuario();

        btnEditarCiudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarCiudadUsuario();
            }
        });
        return view;

    }

    public  void conusultarCiudadUsuario(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaPerfilUsuario/consultarCiudadUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("cuenta_usuario");
                    for (int i = 0; i < jsonArreglo.length(); i++) {

                        String inicializarItem = jsonArreglo.getJSONObject(i).optString("nombre_ciudad");
                        spCiudadUSuario.setSelection(obtenerPosicionItem(spCiudadUSuario, inicializarItem));
                    }
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
                String correoU = correoUsuario;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_usuario", correoU);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void llenarSpinnner() {
        final String ip1 = getString(R.string.ip);
        String url = ip1 + "/findyourstyleBDR/wsJSONCiudad.php";
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
            JSONArray jsonArreglo = jsonObj.optJSONArray("ciudad");
            for (int i = 0; i < jsonArreglo.length(); i++) {
                CategoriaServicio cs = new CategoriaServicio();
                cs.setNombreCategoriaServicio(jsonArreglo.getJSONObject(i).optString("nombre_ciudad"));
                listaCategoriaServicio.add(cs);
            }
            ArrayAdapter<CategoriaServicio> c = new ArrayAdapter<CategoriaServicio>(getContext(), android.R.layout.simple_dropdown_item_1line, listaCategoriaServicio);
            spCiudadUSuario.setAdapter(c);
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

    public  void editarCiudadUsuario(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaPerfilUsuario/editarCiudadUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Ciudad editada exitosamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"La Ciudad no se ha editado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String ciudadU = spCiudadUSuario.getSelectedItem().toString();
                String correoU = correoUsuario;


                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_usuario", correoU);
                parametros.put("nombre_ciudad", ciudadU);

                return parametros;
            }
        };
        request.add(stringRequest);
    }
}
