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

import com.example.findyourstyle.Fragments.DetalleFragment;
import com.example.findyourstyle.Fragments.Horas;
import com.example.findyourstyle.Fragments.InicioClienteFragment;
import com.example.findyourstyle.Fragments.ProductoTiendaFragment;
import com.example.findyourstyle.Interfaces.IDetalleFragment;
import com.example.findyourstyle.Modelo.ProductoTienda;
import com.example.findyourstyle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

public class HomeTiendaActivity extends AppCompatActivity implements IDetalleFragment {

    private Fragment inicioFragment;
    private Fragment productosTienda;
    private Fragment horas;
    private Fragment horasFragment;
    private Fragment perfilFragment;
    private Fragment detalle;
    String correo;
    String enviarCorreo;

    private BottomNavigationView btnNavigationViewTienda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        inicioFragment = new InicioClienteFragment();

        productosTienda = new ProductoTiendaFragment();
        horas = new Horas();
        detalle = new DetalleFragment();

       correo = getIntent().getStringExtra("correoTienda");;
       enviarCorreo = correo;

        final Bundle bundle = new Bundle();
        bundle.putString("correoTienda",correo);
        inicioFragment.setArguments(bundle);

        final Bundle bundleProducatodTienda = new Bundle();
        bundle.putString("correoTienda",correo);
        productosTienda.setArguments(bundle);

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
                        setFragment(productosTienda);
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

    @Override
    public void enviarDetalle(ProductoTienda productoTienda) {

        Bundle bundleEnvio  = new Bundle();
        bundleEnvio.putSerializable("objeto", productoTienda);
        detalle.setArguments(bundleEnvio);



        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragmentTienda, detalle);
        fragmentTransaction.commit();
    }
}
