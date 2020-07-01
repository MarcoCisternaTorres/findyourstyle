package com.example.findyourstyle.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.findyourstyle.Adampters.AdapterProductosTienda;
import com.example.findyourstyle.Modelo.ProductoTienda;
import com.example.findyourstyle.R;

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
        correoTienda = bundle.getString("correoTienda", "No hay correo");
    }


    private TextView nombreProducto, nombreTienda, direccion, precio;
    private ImageView imgDetalle, imgHora, imgEditar;
    Fragment horas, editar;
    RequestQueue request;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle, container, false);

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



        imgHora.setOnClickListener(this);
        imgEditar.setOnClickListener(this);


        Bundle productos = new Bundle(getArguments());
        ProductoTienda productosTienda = null;
        if (productos != null){
            productosTienda = (ProductoTienda) productos.getSerializable("objeto");
            nombreProducto.setText(productosTienda.getNombreProducto());
            precio.setText(productosTienda.getPrecio());

            if(productosTienda.getRutaImagen()!=null){
                //holder.imagen.setImageBitmap(listaProducto.get(position).getImagen());
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

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragmentTienda, fragment);
        fragmentTransaction.commit();
    }
}