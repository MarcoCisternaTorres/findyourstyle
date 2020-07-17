package com.example.findyourstyle.FramentCrudUsuario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
 * Use the {@link EditarApellidoUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarApellidoUsuarioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarApellidoUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarApellidoClienteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarApellidoUsuarioFragment newInstance(String param1, String param2) {
        EditarApellidoUsuarioFragment fragment = new EditarApellidoUsuarioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private StringRequest stringRequest;
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
    String correoUsuario;
    RequestQueue request;
    EditText etxtApellidoUsuario;
    Button btnEditarApellido;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_editar_apellido_cliente, container, false);
        etxtApellidoUsuario = view.findViewById(R.id.atxtApellidoUsuario);
        btnEditarApellido = view.findViewById(R.id.btnEditarApellidoUsuario);
        request = Volley.newRequestQueue(getContext());
        conusultarApellidoUsuario();

        btnEditarApellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarApellidoUsuario();
            }
        });
        return  view;
    }

    public  void conusultarApellidoUsuario(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaPerfilUsuario/consultarApellidoUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("cuenta_usuario");
                    for (int i = 0; i < jsonArreglo.length(); i++) {
                        etxtApellidoUsuario.setText(jsonArreglo.getJSONObject(i).optString("apellido_usuario"));
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

    public  void editarApellidoUsuario(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaPerfilUsuario/editarApellidoUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                etxtApellidoUsuario.setText("");
                Toast.makeText(getContext(),"Apellido editado exitosamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"El apellido no se ha editado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String apellidoU = etxtApellidoUsuario.getText().toString();
                String correoU = correoUsuario;


                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_usuario", correoU);
                parametros.put("apellido_usuario", apellidoU);

                return parametros;
            }
        };
        request.add(stringRequest);
    }
}
