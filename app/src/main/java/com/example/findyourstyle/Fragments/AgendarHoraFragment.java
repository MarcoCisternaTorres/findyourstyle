package com.example.findyourstyle.Fragments;

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

import com.example.findyourstyle.Adampters.AdapterAgendarHora;
import com.example.findyourstyle.Modelo.HorasAtencion;
import com.example.findyourstyle.Modelo.ModeloBuscar;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //cargar Recycler View
    AdapterAgendarHora adapterAgendarHora;
    RecyclerView recyclerAgendarHora;
    ArrayList<HorasAtencion> modelHorasAtencion;

    //declaramos las variables para llamar al objeto
    TextView nombreProducto, nombreTienda, direccion, precio;
    ImageView imgProduto;
    ImageView btnAtras;

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

        //Objeto bundle para recibir los paramentros por argumentos
        Bundle objetoAgendarHora = getArguments();
        ModeloBuscar modeloBuscar = null;

        //Verificar si existen argumentos
        if(objetoAgendarHora != null){
            modeloBuscar = (ModeloBuscar) objetoAgendarHora.getSerializable("objeto");
            //Establecar los datos en ñas vistas
            nombreProducto.setText(modeloBuscar.getNombreProducto());
            nombreTienda.setText(modeloBuscar.getTienda());
            direccion.setText(modeloBuscar.getDireccion());
            //imgProduto.setImageResource(modeloBuscar.getIdImagenBuscar());
        }

        cargarHorasDisponibles();
        mostrarHorasDisponibles();

        return view;
    }

    public void cargarHorasDisponibles(){
        modelHorasAtencion.add(new HorasAtencion("10  DIC","15 HRS"));
        modelHorasAtencion.add(new HorasAtencion("11  DIC","11 HRS"));
        modelHorasAtencion.add(new HorasAtencion("11  DIC","11:30 HRS"));
        modelHorasAtencion.add(new HorasAtencion("12  DIC","15 HRS"));
    }

    public void  mostrarHorasDisponibles() {
        recyclerAgendarHora.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterAgendarHora = new AdapterAgendarHora(getContext(), modelHorasAtencion);
        recyclerAgendarHora.setAdapter(adapterAgendarHora);
    }
}
