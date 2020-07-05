package com.example.findyourstyle.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.findyourstyle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilTiendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilTiendaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilTiendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilTiendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilTiendaFragment newInstance(String param1, String param2) {
        PerfilTiendaFragment fragment = new PerfilTiendaFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Bundle bundle = getArguments();
        correoTienda = bundle.getString("correoTienda", "No hay correo");
    }

    ImageView imgPerfilTienda;
    TextView nombreTienda, direccionTienda, ciudadTienda, categoriaTienda;
    RequestQueue request;
    private StringRequest stringRequest;
    String rutaImagen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_perfil_tienda, container, false);
        imgPerfilTienda = view.findViewById(R.id.imgPerfilFragmentPerfilTienda);
        nombreTienda = view.findViewById(R.id.txtNombre_Tienda_fragmentPerfilTienda);
        direccionTienda = view.findViewById(R.id.txtDireccionFragmentPerfilTienda);
        ciudadTienda = view.findViewById(R.id.CiudadTiendaPerfil);
        categoriaTienda = view.findViewById(R.id.CategoriaTiendaPerfil);

        request = Volley.newRequestQueue(getContext());

        conusultarPerfilTienda();
        return view;
    }

    public  void conusultarPerfilTienda(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultarPerfilTienda.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("tienda");
                    for (int i = 0; i < jsonArreglo.length(); i++) {


                        nombreTienda.setText(jsonArreglo.getJSONObject(i).optString("nombre_tienda"));
                        direccionTienda.setText(jsonArreglo.getJSONObject(i).optString("direccion_tienda"));
                        rutaImagen = jsonArreglo.getJSONObject(i).optString("ruta_imagen");
                        ciudadTienda.setText(jsonArreglo.getJSONObject(i).optString("nombre_ciudad"));
                        categoriaTienda.setText(jsonArreglo.getJSONObject(i).optString("nombre_categoria"));


                        if(rutaImagen!=null){
                            cargarImagenServidor(rutaImagen);
                        }else{
                            imgPerfilTienda.setImageResource(R.drawable.ic_launcher_background);
                        }

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
                String correoT = correoTienda;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
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
                imgPerfilTienda.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }
}