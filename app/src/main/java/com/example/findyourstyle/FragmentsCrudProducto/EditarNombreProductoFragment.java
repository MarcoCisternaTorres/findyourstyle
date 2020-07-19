package com.example.findyourstyle.FragmentsCrudProducto;

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
 * Use the {@link EditarNombreProductoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarNombreProductoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarNombreProductoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarNombreProductoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarNombreProductoFragment newInstance(String param1, String param2) {
        EditarNombreProductoFragment fragment = new EditarNombreProductoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String correoTienda, nombreProducto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        correoTienda = bundle.getString("correoTienda", "No hay correo");
        nombreProducto = bundle.getString("nombreProducto", "No hay nombre");
    }
    EditText etxtNombreProducto;
    Button btnEdtarNombre;
    RequestQueue request;
    private StringRequest stringRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_nombre_producto, container, false);
        etxtNombreProducto = view.findViewById(R.id.etxtEditarNombreProducto);
        btnEdtarNombre = view.findViewById(R.id.btnEditarNombreProducto);
        etxtNombreProducto.setText(nombreProducto);
        request = Volley.newRequestQueue(getContext());

        btnEdtarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarNombreTienda();
            }
        });
        return view;
    }

    public  void editarNombreTienda(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaProductoTienda/editarNombreProducto.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                etxtNombreProducto.setText("");
                Toast.makeText(getContext(),"Nombre editado exitosamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"El nombre no se ha editado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nombreP = etxtNombreProducto.getText().toString();
                String nombreAntiguo = nombreProducto;
                String correoT = correoTienda;


                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
                parametros.put("nombre_producto_antiguo", nombreAntiguo);
                parametros.put("nombre_producto", nombreP);

                return parametros;
            }
        };
        request.add(stringRequest);
    }

}
