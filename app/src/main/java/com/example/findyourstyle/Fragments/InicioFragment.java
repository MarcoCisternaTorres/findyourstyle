package com.example.findyourstyle.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.findyourstyle.Activities.MainActivity;
import com.example.findyourstyle.Adampters.AdapterInicio;
import com.example.findyourstyle.Adampters.AdapterInicioTiendas;
import com.example.findyourstyle.Interfaces.IComunicaFragment;
import com.example.findyourstyle.Modelo.ModelLoMasBuscado;
import com.example.findyourstyle.Modelo.ModelMejoresTiendas;
import com.example.findyourstyle.R;

import java.util.ArrayList;

public class InicioFragment extends Fragment {
    //para los productos recomendados
    AdapterInicio adapterInicio;
    RecyclerView recyclerInicio;
    ArrayList<ModelLoMasBuscado> modelLoMasBuscados;

    // para las mejores tiendas
    AdapterInicioTiendas adapterInicioTiendas;
    RecyclerView recyclerViewTiendas;
    ArrayList<ModelMejoresTiendas> modelMejoresTiendas;

    //Boton Salir
    ImageView btnSalir;

    IComunicaFragment iComunicaFragment;

    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        //para los productos recomendados
        recyclerInicio = view.findViewById(R.id.recycler_view_lo_mas_buscado);
        modelLoMasBuscados = new ArrayList<>();
        //para las mejores tiendas
        recyclerViewTiendas = view.findViewById(R.id.recycler_view_tiendas_recomendadas);
        modelMejoresTiendas = new ArrayList<>();

        //Boton salir
        btnSalir = view.findViewById(R.id.iconSalir_fragmentInicio);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        //para los productos recomendados
        cargarLoMasBuscado();
        mostarLoMasBuscado();
        //para las mejores tiendas
        cargarTiendadRecomendadas();
        mostrarTiendaRecomendadas();

        return view;
    }

    public void cargarLoMasBuscado(){
        modelLoMasBuscados.add(new ModelLoMasBuscado("degradado","Unisex Temuco","$4545",R.drawable.ic_launcher_background));
        modelLoMasBuscados.add(new ModelLoMasBuscado("Nose 1","Unisex Temuco","$4545",R.drawable.ic_launcher_background));
        modelLoMasBuscados.add(new ModelLoMasBuscado("Nose 2","Unisex Temuco","$4545",R.drawable.ic_launcher_background));
        modelLoMasBuscados.add(new ModelLoMasBuscado("Nose 3","Unisex Temuco","$4545",R.drawable.ic_launcher_background));
        modelLoMasBuscados.add(new ModelLoMasBuscado("Nose 4","Unisex Temuco","$4545",R.drawable.ic_launcher_background));
        modelLoMasBuscados.add(new ModelLoMasBuscado("Nose 5","Unisex Temuco","$4545",R.drawable.ic_launcher_background));
    }
    public  void mostarLoMasBuscado(){
        recyclerInicio.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapterInicio = new AdapterInicio(getContext(), modelLoMasBuscados);
        recyclerInicio.setAdapter(adapterInicio);
    }

    public void cargarTiendadRecomendadas(){
        modelMejoresTiendas.add(new ModelMejoresTiendas("UnisexTemuco","4.5","4.5", R.drawable.ic_launcher_background));
        modelMejoresTiendas.add(new ModelMejoresTiendas("UnisexPedro","4.5","4.5", R.drawable.ic_launcher_background));
        modelMejoresTiendas.add(new ModelMejoresTiendas("UnisexMont","4.5","4.5", R.drawable.ic_launcher_background));
        modelMejoresTiendas.add(new ModelMejoresTiendas("UnisexTatoo","4.5","4.5", R.drawable.ic_launcher_background));
        modelMejoresTiendas.add(new ModelMejoresTiendas("UnisexHola","4.5","4.5", R.drawable.ic_launcher_background));
    }

    public void mostrarTiendaRecomendadas(){
        recyclerViewTiendas.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapterInicioTiendas = new AdapterInicioTiendas(getContext(), modelMejoresTiendas);
        recyclerViewTiendas.setAdapter(adapterInicioTiendas);
    }

}
