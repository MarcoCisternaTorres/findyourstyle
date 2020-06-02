package com.example.findyourstyle.Activities;

import com.example.findyourstyle.Interfaces.IComunicaFragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.findyourstyle.Fragments.AgendaFragment;
import com.example.findyourstyle.Fragments.AgendarHoraFragment;
import com.example.findyourstyle.Fragments.BuscarFragment;
import com.example.findyourstyle.Fragments.InicioFragment;
import com.example.findyourstyle.Fragments.PerfilFragment;
import com.example.findyourstyle.Modelo.ModelLoMasBuscado;
import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements IComunicaFragment {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView barraSuperior;

    ArrayList<ModeloBuscar> modeloBuscar;
    ArrayList<ModelLoMasBuscado> modelLoMasBuscados;


    private Fragment inicioFragment;
    private Fragment buscarFragment;
    private Fragment agendaFragment;
    private Fragment perfilFragment;

    ImageView btnAtras;


    private MenuItem menuItem;

    //variables del fragment agendar productos
    AgendarHoraFragment agendarHoraFragment;

    //adcla vhjeashvjs
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView    = (BottomNavigationView) findViewById(R.id.bottomNavegation);
        inicioFragment          = new InicioFragment();
        buscarFragment          = new BuscarFragment();
        agendaFragment          = new AgendaFragment();
        perfilFragment          = new PerfilFragment();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.contenedorFragment,buscarFragment).commit();


        setFragment(inicioFragment);

        modeloBuscar = new ArrayList<>();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.item_inicio:
                        setFragment(inicioFragment);
                        break;

                    case R.id.item_buscar:
                        setFragment(buscarFragment);
                        break;

                    case R.id.item_agenda:
                        setFragment(agendaFragment);
                        break;

                    case R.id.item_perfil:
                        setFragment(perfilFragment);
                        break;

                }

                return true;
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void enviarProducto(ModeloBuscar modeloBuscar) {
        //logica para realizar el envio
        agendarHoraFragment = new AgendarHoraFragment();

        //objeto bundle para transportar la informacion
        Bundle bundleEnvio = new Bundle();

        //enviar objeto con serializable
        bundleEnvio.putSerializable("objeto", modeloBuscar);

        //Pasamos el argumentos a AgendarHoraFrangment
        agendarHoraFragment.setArguments(bundleEnvio);

        //Abir fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, agendarHoraFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void enviarProductosMasBuscados(ModelLoMasBuscado modelLoMasBuscado) {
        //logica para realizar el envio
        agendarHoraFragment = new AgendarHoraFragment();

        //objeto bundle para transportar la informacion
        Bundle bundleEnvio = new Bundle();

        //enviar objeto con serializable
        bundleEnvio.putSerializable("objeto", modelLoMasBuscados);

        //Pasamos el argumentos a AgendarHoraFrangment
        agendarHoraFragment.setArguments(bundleEnvio);

        //Abir fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, agendarHoraFragment);
        fragmentTransaction.commit();
    }
}
