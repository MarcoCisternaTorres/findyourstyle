package com.example.findyourstyle.FragmentCrudTienda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.R;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarImagenTiendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarImagenTiendaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarImagenTiendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarImagenTiendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarImagenTiendaFragment newInstance(String param1, String param2) {
        EditarImagenTiendaFragment fragment = new EditarImagenTiendaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String correoTienda, nombreTienda;
    Button btnEditarImagen;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle = getArguments();
        correoTienda = bundle.getString("correoTienda", "No hay correo");
        nombreTienda = bundle.getString("nombreTienda", "No hay nombre");
    }
    ImageView imgTienda, imgAgregarImagen;
    private Bitmap bitmap;
    RequestQueue request;
    private StringRequest stringRequest;
    String rutaImagen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_imagen_tienda, container, false);
        imgTienda = view.findViewById(R.id.imgEditarPerfilTienda);
        imgAgregarImagen = view.findViewById(R.id.imgEditarImagenPerfilTienda);
        btnEditarImagen = view.findViewById(R.id.btnEditarImagenTienda);

        request = Volley.newRequestQueue(getContext());
        conusultarImagenTienda();

        imgAgregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });
        btnEditarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarImagen();
            }
        });
        return view;

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
            imgTienda.setImageURI(path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                imgTienda.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap=redimensionarImagen(bitmap,800,800);
        }

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

    public  void conusultarImagenTienda(){

        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaPerfilTienda/consultarImagenPerfilTienda.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArreglo = jsonObj.optJSONArray("tienda");
                    for (int i = 0; i < jsonArreglo.length(); i++) {
                        rutaImagen = jsonArreglo.getJSONObject(i).optString("ruta_imagen");
                        if(rutaImagen!=null){
                            cargarImagenServidor(rutaImagen);
                        }else{
                            imgTienda.setImageResource(R.drawable.ic_launcher_background);
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
                String correoT = correoTienda;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void cargarImagenServidor(String rutaImagen){
        String ip=getActivity().getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgTienda.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Imagen no registrada", Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }

    public  void editarImagen(){
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip + "/findyourstyleBDR/consultaPerfilTienda/editarImagenTienda.php?";

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
                String correoT = correoTienda;
                String nombreT = nombreTienda;
                String imagenT = convertirImagenString(bitmap);


                Map<String,String> parametros = new HashMap<>();
                parametros.put("correo_tienda", correoT);
                parametros.put("nombre_tienda", nombreT);
                parametros.put("imagen", imagenT);
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
