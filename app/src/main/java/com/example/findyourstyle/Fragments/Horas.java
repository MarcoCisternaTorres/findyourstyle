package com.example.findyourstyle.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.R;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Horas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Horas extends Fragment  implements  View.OnClickListener , Response.Listener<JSONObject>,Response.ErrorListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Horas() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Horas.
     */
    // TODO: Rename and change types and number of parameters
    public static Horas newInstance(String param1, String param2) {
        Horas fragment = new Horas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    String nombreProducto;
    String correoTienda;
    AlertDialog.Builder alert;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        nombreProducto = bundle.getString("nombreProducto", "No hay nombre");
        correoTienda = bundle.getString("correoTienda", "No hay correo");
        alert = new AlertDialog.Builder(getContext());
    }

    ProgressDialog progreso;
    RequestQueue request, requestSpinner;
    JsonObjectRequest jsonObjectRequest, jsonObjectRequestSpninner;
    ImageView iconCalendar_AgregarFecha,iconClock_AgregarHora, iconTick, imgVolverAtras;
    TextView txtFecha_carAgregarFecha,txtFecha_carAgregarHora;
    String diaAtencion;
    String horaAtencion;
    DetalleFragment detalleFragment;


    private int dia,mes,ano,hora,minutos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horas,container, false);
        iconCalendar_AgregarFecha= view.findViewById(R.id.iconCalendar_AgregarFecha);
        iconClock_AgregarHora = view.findViewById(R.id.iconClock_AgregarHora);
        iconTick = view.findViewById(R.id.iconTick_carrAgregarHora);
        txtFecha_carAgregarFecha= view.findViewById(R.id.txtFecha_carAgregarFecha);
        txtFecha_carAgregarHora=view.findViewById(R.id.txtHora_carAgregarHora);
        //imgVolverAtras = view.findViewById(R.id.iconSalir_fragmentInicioback);
        detalleFragment = new DetalleFragment();


        request = Volley.newRequestQueue(getContext());

        iconCalendar_AgregarFecha.setOnClickListener(this);
        iconClock_AgregarHora.setOnClickListener(this);
        iconTick.setOnClickListener(this);
        //imgVolverAtras.setOnClickListener(this);

        final Bundle bundleNombreProducto = new Bundle();
        bundleNombreProducto.putString("nombreProducto",nombreProducto);
        detalleFragment.setArguments(bundleNombreProducto);

        final Bundle bundle = new Bundle();
        bundle.putString("correoTienda",correoTienda);
        detalleFragment.setArguments(bundle);

        // Inflate the layout for this fragment
        return view;
    }

   @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v==iconCalendar_AgregarFecha){
            final Calendar c =Calendar.getInstance();
            dia =c.get(Calendar.DAY_OF_MONTH);
            mes =c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    txtFecha_carAgregarFecha.setText(dayOfMonth+"/"+(monthOfYear)+"/"+year);
                    diaAtencion = dayOfMonth+"/"+monthOfYear+"/"+year;
                }
            },dia,mes,ano);
            datePickerDialog.show();

        }

        if (v==iconClock_AgregarHora){
             final Calendar c =Calendar.getInstance();
             hora=c.get(Calendar.HOUR_OF_DAY);
             minutos= c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog= new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtFecha_carAgregarHora.setText(hourOfDay+":"+minute);
                    horaAtencion = hourOfDay+":"+minute;
            }
            }
            ,hora,minutos,false);
            timePickerDialog.show();
        }

        if (v == iconTick) {
            if (horaAtencion != null && diaAtencion != null) {
                alert.setMessage("Desea agregar la hora?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        agregarHoraBaseDatos();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog titulo = alert.create();
                titulo.setTitle("Alerta");
                titulo.show();
            }else {
                Toast.makeText(getContext(),"Debe ingresar una Hora y Fecha de atención", Toast.LENGTH_SHORT).show();
            }
        }

        /*if (v == imgVolverAtras){
            setFragment(detalleFragment);
        }*/
    }



    private void  agregarHoraBaseDatos(){
        //Barra de progreso
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();
        // Enviar datos al web service
        final String ip = getString(R.string.ip);
        String url = ip +"/findyourstyleBDR/agregarHoraAtencion.php?dia_atencion="+diaAtencion.toString()+
                "&hora_atencion="+horaAtencion.toString()+"&nombre_producto="+nombreProducto.toString()+"&correo_tienda="+correoTienda.toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(),"No se puede registrar"+error.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        progreso.hide();
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragmentTienda, fragment);
        fragmentTransaction.commit();
    }
}