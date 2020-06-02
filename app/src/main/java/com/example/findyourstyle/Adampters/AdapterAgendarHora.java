package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.findyourstyle.Modelo.HorasAtencion;
import com.example.findyourstyle.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAgendarHora extends RecyclerView.Adapter<AdapterAgendarHora.ViewHolder> {

    LayoutInflater inflaterAgendarHora;
    ArrayList<HorasAtencion> modelHorasAtencion;

    public AdapterAgendarHora(Context context, ArrayList<HorasAtencion> modelHorasAtencion){
        this.inflaterAgendarHora = LayoutInflater.from(context);
        this.modelHorasAtencion = modelHorasAtencion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflaterAgendarHora.inflate(R.layout.card_view_agendar_hora, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fechaAtencion = modelHorasAtencion.get(position).getFecha();
        String horaAtencion = modelHorasAtencion.get(position).getHora();

        holder.txtFecha.setText(fechaAtencion);
        holder.textHora.setText(horaAtencion);
    }

    @Override
    public int getItemCount() {
        return modelHorasAtencion.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtFecha, textHora;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFecha = itemView.findViewById(R.id.txtFecha_carAgendarHora);
            textHora = itemView.findViewById(R.id.txtHora_carAgendarHora);

        }
    }
}
