package com.example.findyourstyle.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Adampters.AdapterAgendarHora;
import com.example.findyourstyle.Modelo.HorasAtencion;
import com.example.findyourstyle.Modelo.ModelLoMasBuscado;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.Modelo.ProductoTienda;
import com.example.findyourstyle.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgendarHoraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendarHoraFragment extends Fragment {
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
    TextView nombreProducto, nombreTienda, direccion, precio;
    ImageView imgProduto;
    ImageView btnAtras;
    RequestQueue request;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agendar_hora, container, false);

        //Recycler agendar horas
        recyclerAgendarHora = view.findViewById(R.id.recyclerView_agendarHora);
        modelHorasAtencion = new ArrayList<>();
        //para la imagen y descripcion del producto
        nombreProducto = view.findViewById(R.id.txtNombreProducto_agendarHora);
        nombreTienda = view.findViewById(R.id.txtTienda_agendarHora);
        direccion = view.findViewById(R.id.txtDireccion_agendarHora);
        precio = view.findViewById(R.id.txtPrecio_agendarHora);
        imgProduto = view.findViewById(R.id.imgAgendarHora);
        btnAtras = view.findViewById(R.id.fechaAtras_agendarHora);
        request = Volley.newRequestQueue(getContext());


        Bundle productosHora = new Bundle(getArguments());
        ModeloBuscar productos = null;
        if (productosHora != null){
            productos = (ModeloBuscar) productosHora.getSerializable("productos");
            nombreProducto.setText(productos.getNombreProducto());
            nombreTienda.setText(productos.getNombreProducto());
            direccion.setText(productos.getDireccion());
            precio.setText(productos.getPrecio());

            if(productos.getRutaImagen()!=null){
                //holder.imagen.setImageBitmap(listaProducto.get(position).getImagen());
                cargarImagenServidor(productos.getRutaImagen());
            }else{
                imgProduto.setImageResource(R.drawable.ic_launcher_background);
            }
        }


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
                Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }
}
