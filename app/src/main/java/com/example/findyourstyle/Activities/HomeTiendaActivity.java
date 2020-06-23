package com.example.findyourstyle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.findyourstyle.Fragments.Horas;
import com.example.findyourstyle.Fragments.InicioClienteFragment;
import com.example.findyourstyle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeTiendaActivity extends AppCompatActivity {

    private Fragment inicioFragment;
    private Fragment horas;
    private Fragment horasFragment;
    private Fragment perfilFragment;

    private BottomNavigationView btnNavigationViewTienda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        inicioFragment = new InicioClienteFragment();
        horas = new Horas();

        btnNavigationViewTienda = findViewById(R.id.bottomNavegationTienda);



        setFragment(inicioFragment);

        btnNavigationViewTienda.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.item_inicio:
                        setFragment(inicioFragment);
                        break;
                    case R.id.item_productos:
                        setFragment(horas);
                }
                return true;
            }
        });
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragmentTienda, fragment);
        fragmentTransaction.commit();
    }
}
