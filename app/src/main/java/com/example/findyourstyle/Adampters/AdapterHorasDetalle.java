package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.HorasTiendaDetalle;
import com.example.findyourstyle.Modelo.HorasUsuario;
import com.example.findyourstyle.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterHorasDetalle extends RecyclerView.Adapter<AdapterHorasDetalle.horasDetalleHolder>  {
    List<HorasTiendaDetalle> listaHorasDetalle;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public AdapterHorasDetalle(List<HorasTiendaDetalle> listaHorasDetalle, Context context) {
        this.listaHorasDetalle = listaHorasDetalle;
        this.context = context;
        request = Volley.newRequestQueue(context);

    }

    @NonNull
    @Override
    public horasDetalleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_horas_detalle,parent,false);
        /*RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);*/

        return new horasDetalleHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull horasDetalleHolder holder, int position) {
        holder.horaAtencion.setText(listaHorasDetalle.get(position).getHora_atencion());
        holder.diaAtencion.setText(listaHorasDetalle.get(position).getFecha_atencion());
    }

    @Override
    public int getItemCount() {
        return listaHorasDetalle.size();
    }


    public  class horasDetalleHolder extends  RecyclerView.ViewHolder{
        TextView horaAtencion, diaAtencion;
        public horasDetalleHolder(@NonNull View itemView) {
            super(itemView);
            diaAtencion = itemView.findViewById(R.id.txtFechaDetalleHora);
            horaAtencion = itemView.findViewById(R.id.txtHoraDetalleHora);
        }
    }
}
