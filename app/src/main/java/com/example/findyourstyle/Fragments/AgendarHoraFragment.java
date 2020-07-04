package com.example.findyourstyle.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Adampters.AdapterAgendarHora;
import com.example.findyourstyle.Adampters.AdapterHorasUsuario;
import com.example.findyourstyle.Modelo.CategoriaServicio;
import com.example.findyourstyle.Modelo.HorasAtencion;
import com.example.findyourstyle.Modelo.HorasUsuario;
import com.example.findyourstyle.Modelo.ModelLoMasBuscado;
import com.example.findyourstyle.Modelo.ModeloBuscar;
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
 * Use the {@link AgendarHoraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendarHoraFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgendarHoraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgendarHoraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgendarHoraFragment newInstance(String param1, String param2) {
        AgendarHoraFragment fragment = new AgendarHoraFragment();
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
        Bundle bundle = getArguments();
        correoUsuario = bundle.getString("correoUsuario", "No hay correo");
    }

    //cargar Recycler View
    AdapterAgendarHora adapterAgendarHora;
    RecyclerView recyclerAgendarHora;
    ArrayList<HorasAtencion> modelHorasAtencion;

    //declaramos las variables para llamar al objeto
    TextView nombreProducto, nombreTienda, direccion, precio, txtMesajeNoHayHoras;
    ImageView imgProduto;
    ImageView btnAtras;
    RequestQueue request;
    ArrayList<HorasUsuario> listaHorasUsuario;
    private StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    AlertDialog.Builder alert;
    String fecha_atencion;
    String hora_atencion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agendar_hora, container, false);

        //Recycler agendar horas
        recyclerAgendarHora = view.findViewById(R.id.recyclerViewAgendarHora);
        recyclerAgendarHora.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerAgendarHora.setHasFixedSize(true);
        recyclerAgendarHora.setLayoutManager(new GridLayoutManager(getContext(), 1));

        txtMesajeNoHayHoras = view.findViewById(R.id.mensajeNoHayHoras);
        modelHorasAtencion = new ArrayList<>();
        //para la imagen y descripcion del producto
        nombreProducto = view.findViewById(R.id.txtNombreProducto_agendarHora);
        nombreTienda = view.findViewById(R.id.txtTienda_agendarHora);
        direccion = view.findViewById(R.id.txtDireccion_agendarHora);
        precio = view.findViewById(R.id.txtPrecio_agendarHora);
        imgProduto = view.findViewById(R.id.imgAgendarHora);
        btnAtras = view.findViewById(R.id.fechaAtras_agendarHora);
        request = Volley.newRequestQueue(getContext());
        listaHorasUsuario = new ArrayList<>();
        alert = new AlertDialog.Builder(getContext());

        Bundle productosHora = new Bundle(getArguments());
        ModeloBuscar productos = null;
        if (productosHora != null){
            productos = (ModeloBuscar) productosHora.getSerializable("productos");
            nombreProducto.setText(productos.getNombreProducto());
            nombreTienda.setText(productos.getTienda());
            direccion.setText(productos.getDireccion());
            precio.setText(productos.getPrecio());

            if(productos.getRutaImagen()!=null){
                //holder.imagen.setImageBitmap(listaProducto.get(position).getImagen());
                cargarImagenServidor(productos.getRutaImagen());
            }else{
                imgProduto.setImageResource(R.drawable.ic_launcher_background);
            }

        }
        cargarHorasDisponibles();

        //Icono para volver atras
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarFragment buscarFragment = new BuscarFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorFragment, buscarFragment);
                transaction.commit();
            }
        });

        return view;
    }

    private void cargarImagenServidor(String rutaImagen){
        String ip=getActivity().getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgProduto.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "no se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }

    private void cargarHorasDisponibles() {

        final String ip = getString(R.string.ip);
        String nombreTi = nombreTienda.getText().toString();
        String nombrePro= nombreProducto.getText().toString();

        String url = ip + "/findyourstyleBDR/consultarHorasUsuarios.php?nombre_tienda="+nombreTi+"&nombre_producto="+nombrePro;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        txtMesajeNoHayHoras.setText("No hay horas disponibles");
        System.out.println();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        HorasUsuario horasUsuario = null;
        JSONArray jsonArray =  response.optJSONArray("hora_atencion");
        try{
            for(int i = 0; i < jsonArray.length();i++){
                horasUsuario = new HorasUsuario();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);

                horasUsuario.setHora_atencion(jsonObject.optString("hora_atencion"));
                horasUsuario.setFecha_atencion(jsonObject.optString("dia_atencion"));

                listaHorasUsuario.add(horasUsuario);
            }


            AdapterHorasUsuario adapterHorasUsuario = new AdapterHorasUsuario(listaHorasUsuario,getContext());
            recyclerAgendarHora.setAdapter(adapterHorasUsuario);
            adapterHorasUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fecha_atencion= listaHorasUsuario.get(recyclerAgendarHora.getChildAdapterPosition(v)).getFecha_atencion();
                     hora_atencion= listaHorasUsuario.get(recyclerAgendarHora.getChildAdapterPosition(v)).getHora_atencion();
alert.setMessage("¿Desea agendar esta hora de atencion?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reservarHoraAtencion(fecha_atencion, hora_atencion);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
});
                    AlertDialog titulo = alert.create();
                    titulo.setTitle("Alerta");
                    titulo.show();


                }
            });

        }catch (JSONException e){
            e.printStackTrace();

        }
    }

    public  void reservarHoraAtencion(final String fecha_atencion, final String hora_atencion){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/agendarHora.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Hora reservada exitosamente", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Hora no reservada", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correoU = correoUsuario;
                String nombreP = nombreProducto.getText().toString();
                String nombreT = nombreTienda.getText().toString();
                String fechaA = fecha_atencion;
                String horaA = hora_atencion;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_usuario", correoU);
                parametros.put("nombre_producto", nombreP);
                parametros.put("nombre_tienda",nombreT );
                parametros.put("dia_atencion", fechaA);
                parametros.put("hora_atencion", horaA);

                return parametros;
            }
        };
        request.add(stringRequest);

    }


}
