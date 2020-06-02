package com.example.findyourstyle.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.findyourstyle.Activities.HomeActivity;
import com.example.findyourstyle.Interfaces.IComunicaFragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.findyourstyle.Adampters.AdapterBuscar;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarFragment extends Fragment   {

    AdapterBuscar adapterBuscar;
    RecyclerView recyclerViewBuscar;
    ArrayList<ModeloBuscar> productos;

    ImageView btnAtras;


    //comunicacion de fragments
    Activity actividad;
    IComunicaFragment iComunicaFragment;


    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view    = inflater.inflate(R.layout.fragment_buscar, container, false);
        recyclerViewBuscar = view.findViewById(R.id.recycler_view_buscar);
        productos    = new ArrayList<>();
        btnAtras = view.findViewById(R.id.fechaAtras_fragmentBuscar);
        cargarProductos();
        mostarData();

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InicioFragment inicioFragment = new InicioFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorFragment, inicioFragment);
                transaction.commit();
            }
        });



        return view;
    }

    public void cargarProductos(){
        productos.add(new ModeloBuscar("degradado","UnisexTemuco","Balmaceda 0888","$ 5156",R.drawable.ic_launcher_background));
        productos.add(new ModeloBuscar("Pintura","UnisexTemuco","Balmaceda 0888","$ 5156",R.drawable.ic_launcher_background));
        productos.add(new ModeloBuscar("Alisado","UnisexTemuco","Balmaceda 0888","$ 5156",R.drawable.ic_launcher_background));
        productos.add(new ModeloBuscar("Placha","UnisexTemuco","Balmaceda 0888","$ 5156",R.drawable.ic_launcher_background));
        productos.add(new ModeloBuscar("Pediqure","UnisexTemuco","Balmaceda 0888","$ 5156",R.drawable.ic_launcher_background));
        productos.add(new ModeloBuscar("Nose","UnisexTemuco","Balmaceda 0888","$ 5156",R.drawable.ic_launcher_background));
        productos.add(new ModeloBuscar("Nose X2","UnisexTemuco","Balmaceda 0888","$ 5156",R.drawable.ic_launcher_background));
    }

    public void mostarData(){
        recyclerViewBuscar.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterBuscar = new AdapterBuscar(getContext(), productos);
        recyclerViewBuscar.setAdapter(adapterBuscar);

        adapterBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = productos.get(recyclerViewBuscar.getChildAdapterPosition(view)).getNombreProducto();
                Toast.makeText(getContext(),"selecion"+nombre, Toast.LENGTH_SHORT).show();
                iComunicaFragment.enviarProducto(productos.get(recyclerViewBuscar.getChildAdapterPosition(view)));
            }
        });
    }

    // comunicacion de fragment a fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.actividad = (Activity) context;
            iComunicaFragment = (IComunicaFragment) this.actividad;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

  }
