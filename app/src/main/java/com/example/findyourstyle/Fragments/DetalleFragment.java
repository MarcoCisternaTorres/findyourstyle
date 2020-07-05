package com.example.findyourstyle.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.findyourstyle.Adampters.AdapterAgendaUsuario;
import com.example.findyourstyle.Adampters.AdapterHorasDetalle;
import com.example.findyourstyle.Adampters.AdapterProductosTienda;
import com.example.findyourstyle.Modelo.HorasTiendaDetalle;
import com.example.findyourstyle.Modelo.HorasUsuario;
import com.example.findyourstyle.Modelo.ProductoTienda;
import com.example.findyourstyle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleFragment extends Fragment implements  View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleFragment newInstance(String param1, String param2) {
        DetalleFragment fragment = new DetalleFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        correoTienda = bundle.getString("correoTienda", "No hay correo");
    }


    private TextView nombreProducto, nombreTienda, direccion, precio;
    private ImageView imgDetalle, imgHora, imgEditar;
    Fragment horas, editar;
    RequestQueue request;
    private StringRequest stringRequest;
    RecyclerView recyclerHorasProductos;
    ArrayList<HorasTiendaDetalle> listaHorasDetalle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle, container, false);
        recyclerHorasProductos = view.findViewById(R.id.recyclerViewDetalleHora);
        recyclerHorasProductos.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerHorasProductos.setHasFixedSize(true);
        recyclerHorasProductos.setLayoutManager(new GridLayoutManager(getContext(), 1));
        listaHorasDetalle = new ArrayList<>();

        nombreProducto = view.findViewById(R.id.txtNombreProductoDetalle);
        precio = view.findViewById(R.id.txtPrecioDetalle);
        imgDetalle = view.findViewById(R.id.imgDetalle);
        imgHora = view.findViewById(R.id.imgAgregarHoraDetalle);
        imgEditar = view.findViewById(R.id.imgEditarproducto);
        request = Volley.newRequestQueue(getContext());
        horas = new Horas();
        editar = new EditarYEliminarProductos();

        final Bundle bundle = new Bundle();
        bundle.putString("correoTienda",correoTienda);
        horas.setArguments(bundle);
        consultarHoraAtencionProductos();


        imgHora.setOnClickListener(this);
        imgEditar.setOnClickListener(this);


        Bundle productos = new Bundle(getArguments());
        ProductoTienda productosTienda = null;
        if (productos != null){
            productosTienda = (ProductoTienda) productos.getSerializable("objeto");
            nombreProducto.setText(productosTienda.getNombreProducto());
            precio.setText(productosTienda.getPrecio());

            if(productosTienda.getRutaImagen()!=null){
                cargarImagenServidor(productosTienda.getRutaImagen());
            }else{
                imgDetalle.setImageResource(R.drawable.ic_launcher_background);
            }

            final Bundle bundleNombreHoras = new Bundle();
            bundleNombreHoras.putString("nombreProducto",productosTienda.getNombreProducto());
            bundleNombreHoras.putString("correoTienda",correoTienda);
            horas.setArguments(bundleNombreHoras);

            final Bundle bundleEditarProducto= new Bundle();
        bundleEditarProducto.putString("nombreProducto",productosTienda.getNombreProducto());
        bundleEditarProducto.putString("correoTienda",correoTienda);
        editar.setArguments(bundleEditarProducto);


    }


        return view;
    }
    private void cargarImagenServidor(String rutaImagen){
        String ip=getActivity().getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgDetalle.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == imgHora){
            setFragment(horas);
        }
        if(v == imgEditar){
            setFragment(editar);
        }
    }

    public  void consultarHoraAtencionProductos(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultarHorasPorProductos.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HorasTiendaDetalle horasTiendaDetalle = null;
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("hora_atencion");
                    for (int i = 0; i < jsonArreglo.length(); i++) {
                        horasTiendaDetalle = new HorasTiendaDetalle();
                        horasTiendaDetalle.setFecha_atencion(jsonArreglo.getJSONObject(i).optString("dia_atencion"));
                        horasTiendaDetalle.setHora_atencion(jsonArreglo.getJSONObject(i).optString("hora_atencion"));
                        listaHorasDetalle.add(horasTiendaDetalle);
                    }
                    AdapterHorasDetalle adapterHorasDetalle = new AdapterHorasDetalle(listaHorasDetalle,getContext());
                    recyclerHorasProductos.setAdapter(adapterHorasDetalle);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No ha registrado ninguna hora de atención", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correoT = correoTienda;
                String nombreP = nombreProducto.getText().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
                parametros.put("nombre_producto", nombreP);
                return parametros;
            }
        };
        request.add(stringRequest);

    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragmentTienda, fragment);
        fragmentTransaction.commit();
    }
}
