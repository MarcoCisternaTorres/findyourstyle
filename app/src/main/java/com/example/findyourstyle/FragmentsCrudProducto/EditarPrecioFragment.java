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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPrecioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPrecioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarPrecioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarPrecioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarPrecioFragment newInstance(String param1, String param2) {
        EditarPrecioFragment fragment = new EditarPrecioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String correoTienda, nombreProducto, precioProducto;
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
        precioProducto = bundle.getString("precioProducto", "No hay precio");
    }
    EditText etxtPrecioProducto;
    RequestQueue request;
    private StringRequest stringRequest;
    Button btnEditarPrecio;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_precio, container, false);
        etxtPrecioProducto = view.findViewById(R.id.etxtEditarPrecioProducto);
        btnEditarPrecio = view.findViewById(R.id.btnEditarPrecioProducto);
        etxtPrecioProducto.setText(precioProducto);
        request = Volley.newRequestQueue(getContext());
        btnEditarPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPrecioTienda();
            }
        });

        return  view;
    }

    public  void editarPrecioTienda(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaProductoTienda/editarPrecioProducto.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                etxtPrecioProducto.setText("");
                Toast.makeText(getContext(),"Precio editado exitosamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"El precio no se ha editado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nombreP = nombreProducto;
                String precio = etxtPrecioProducto.getText().toString();
                String correoT = correoTienda;


                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
                parametros.put("precio", precio);
                parametros.put("nombre_producto", nombreP);

                return parametros;
            }
        };
        request.add(stringRequest);
    }
}
