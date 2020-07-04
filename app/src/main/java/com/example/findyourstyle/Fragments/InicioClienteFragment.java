package com.example.findyourstyle.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findyourstyle.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioClienteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioClienteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioClienteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioCliente.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioClienteFragment newInstance(String param1, String param2) {
        InicioClienteFragment fragment = new InicioClienteFragment();
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
            correoTienda = getArguments().getString("correoTienda", "No hay correo");
        }
    }
    AgregarProductoFragment agregarProductoFragment;
    ProductoTiendaFragment productoTiendaFragment;
    HorasAgendadasTiendaFragment horasAgendadasTiendaFragment;

    private ImageView imgAgregarProducto, imgHorasAgendadas;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false);
        agregarProductoFragment = new AgregarProductoFragment();
        productoTiendaFragment = new ProductoTiendaFragment();
        horasAgendadasTiendaFragment = new HorasAgendadasTiendaFragment();
        imgAgregarProducto = view.findViewById(R.id.imgCrearNuevoProducto);
        imgHorasAgendadas = view.findViewById(R.id.imgHorasAgnedadas);

        final Bundle bundle = new Bundle();
        bundle.putString("correoTienda",correoTienda);
        agregarProductoFragment.setArguments(bundle);

        final Bundle bundleCorreoHorasAgendadas = new Bundle();
        bundleCorreoHorasAgendadas.putString("correoTienda",correoTienda);
        horasAgendadasTiendaFragment.setArguments(bundleCorreoHorasAgendadas);

        imgAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(agregarProductoFragment);
            }
        });

        imgHorasAgendadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(horasAgendadasTiendaFragment);
            }
        });
        return view;
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragmentTienda, fragment);
        fragmentTransaction.commit();
    }
}