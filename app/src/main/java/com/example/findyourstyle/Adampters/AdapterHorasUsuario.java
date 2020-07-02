package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.HorasUsuario;
import com.example.findyourstyle.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterHorasUsuario extends RecyclerView.Adapter<AdapterHorasUsuario.horasUsuariosHolder> implements View.OnClickListener{

    List<HorasUsuario> listaHorasUsuarios;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public AdapterHorasUsuario(List<HorasUsuario> listaHorasUsuarios, Context context) {
        this.listaHorasUsuarios = listaHorasUsuarios;
        this.context = context;
        request = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public horasUsuariosHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_agendar_hora,parent,false);
        /*RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);*/
        vista.setOnClickListener(this);
        return new horasUsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull horasUsuariosHolder holder, int position) {
        holder.horaAtencion.setText(listaHorasUsuarios.get(position).getHora_atencion());
        holder.diaAtencion.setText(listaHorasUsuarios.get(position).getFecha_atencion());
    }

    @Override
    public int getItemCount() {
        return listaHorasUsuarios.size();
    }

    public void  setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    public  class horasUsuariosHolder extends RecyclerView.ViewHolder{
        TextView horaAtencion, diaAtencion;

        public horasUsuariosHolder(@NonNull View itemView) {
            super(itemView);
            diaAtencion = itemView.findViewById(R.id.txtFechaAgendarHora);
            horaAtencion = itemView.findViewById(R.id.txtHoraAgendarHora);
        }
    }
}
