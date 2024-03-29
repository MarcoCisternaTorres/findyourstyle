package com.example.findyourstyle.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Activities.HomeActivity;
import com.example.findyourstyle.Activities.MainActivity;
import com.example.findyourstyle.BuildConfig;
import com.example.findyourstyle.FramentCrudUsuario.EditarApellidoUsuarioFragment;
import com.example.findyourstyle.FramentCrudUsuario.EditarCiudadUsuarioFragment;
import com.example.findyourstyle.FramentCrudUsuario.EditarNombreUsuarioFragment;
import com.example.findyourstyle.R;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarPerfilUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }








    final int COD_SELECCIONA=10;
    final int COD_FOTO = 20;
    final String CARPETA_RAIZ = "MisFotosApp";
    final String CARPETA_IMAGENES = "imagenes";
    final String RUTA_IMAGEN = CARPETA_RAIZ + CARPETA_IMAGENES;
    String path;


    public PerfilFragment() {
        // Required empty public constructor
    }

    String correoUsuario;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Bundle bundle = getArguments();
            correoUsuario = bundle.getString("correoUsuario", "No hay correo");

        }


    }
    private ImageView imgPerfil, imgEditarImagen;
    private Button btnCerrar, btnEditarPerfil;
    TextView txtNombreUsuario, txtApellidoUsuario, txtNombreCiudad;
    RequestQueue request;
    private StringRequest stringRequest;
    String rutaImagen;
    private Bitmap bitmap;
    private Fragment perfilFragment, editarPerfilUsuario, editarNombre, editarApellido, editarCiudad;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        imgPerfil = view.findViewById(R.id.imgPerfilFragmentPerfil);
        imgEditarImagen = view.findViewById(R.id.imgEditarImagenUsuario);
        txtNombreUsuario = view.findViewById(R.id.txtNombreFragmentPerfil);
        txtApellidoUsuario = view.findViewById(R.id.txtApellidoFragmentPerfil);
        txtNombreCiudad = view.findViewById(R.id.NombreCiudadUsuario);
        btnCerrar = view.findViewById(R.id.btnCerrarSesionUsuario);
        //btnEditarPerfil =view.findViewById(R.id.btnEditarUsuario);
        request = Volley.newRequestQueue(getContext());


        perfilFragment = new PerfilFragment();
        editarPerfilUsuario = new EditarPerfilUsuarioFragment();
        editarNombre = new EditarNombreUsuarioFragment();
        editarApellido = new EditarApellidoUsuarioFragment();
        editarCiudad = new EditarCiudadUsuarioFragment();

        final Bundle bundleEditarUsuario = new Bundle();
        bundleEditarUsuario.putString("correoUsuario",correoUsuario);
        editarPerfilUsuario.setArguments(bundleEditarUsuario);


        final Bundle bundleEditarNombre = new Bundle();
        bundleEditarNombre.putString("correoUsuario",correoUsuario);
        editarNombre.setArguments(bundleEditarNombre);


        final Bundle bundleEditarApellido = new Bundle();
        bundleEditarApellido.putString("correoUsuario",correoUsuario);
        editarApellido.setArguments(bundleEditarApellido);

        final Bundle bundleEditarCiudad = new Bundle();
        bundleEditarCiudad.putString("correoUsuario",correoUsuario);
        editarCiudad.setArguments(bundleEditarCiudad);

        conusultarPerfilUsuario();


       /* btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(editarPerfilUsuario);
            }
        });*/
       txtNombreUsuario.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setFragment(editarNombre);
           }
       });
       txtApellidoUsuario.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setFragment(editarApellido);
           }
       });
       txtNombreCiudad.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setFragment(editarCiudad);
           }
       });
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        imgEditarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    public  void conusultarPerfilUsuario(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultarPerfilUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("cuenta_usuario");
                    for (int i = 0; i < jsonArreglo.length(); i++) {


                        txtNombreUsuario.setText(jsonArreglo.getJSONObject(i).optString("nombre_usuario"));
                        txtApellidoUsuario.setText(jsonArreglo.getJSONObject(i).optString("apellido_usuario"));
                        rutaImagen = jsonArreglo.getJSONObject(i).optString("ruta_imagen");
                        txtNombreCiudad.setText(jsonArreglo.getJSONObject(i).optString("nombre_ciudad"));

                        if(rutaImagen!=null){
                            cargarImagenServidor(rutaImagen);
                        }else{
                            imgPerfil.setImageResource(R.drawable.ic_launcher_background);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No ha conectado", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correoU = correoUsuario;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_usuario", correoU);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void cargarImagenServidor(String rutaImagen){
        String ip=getActivity().getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/consultaPerfilUsuario/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgPerfil.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Imagen no registrada", Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }



    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, fragment);
        fragmentTransaction.commit();
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione una Apliacación"), 10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            Uri path = data.getData();
            imgPerfil.setImageURI(path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                imgPerfil.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap=redimensionarImagen(bitmap,800,800);
        }
        editarImagen();
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }

    }

    public  void editarImagen(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaPerfilUsuario/editarImagenUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Imagen editada exitosamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"La imagen no se ha editado", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correoU = correoUsuario;
                String nombreU = txtNombreUsuario.getText().toString();
                String imagenU = convertirImagenString(bitmap);


                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_usuario", correoU);
                parametros.put("nombre_usuario", nombreU);
                parametros.put("imagen", imagenU);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private String convertirImagenString(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte [] imageByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imageByte,Base64.DEFAULT);


        return imagenString;
    }

}
