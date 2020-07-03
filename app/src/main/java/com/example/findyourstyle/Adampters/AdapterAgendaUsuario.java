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

public class AdapterAgendaUsuario extends RecyclerView.Adapter<AdapterAgendaUsuario.agendaUsuariosHolder> implements  View.OnClickListener{

    List<HorasUsuario> listaHoraUsuario;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public AdapterAgendaUsuario(List<HorasUsuario> listaHoraUsuario, Context context) {
        this.listaHoraUsuario = listaHoraUsuario;
        this.context = context;
        request = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public agendaUsuariosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_agenda_usuario,parent,false);
        vista.setOnClickListener(this);
        return new agendaUsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull agendaUsuariosHolder holder, int position) {
        holder.horaAtencion.setText(listaHoraUsuario.get(position).getHora_atencion());
        holder.diaAtencion.setText(listaHoraUsuario.get(position).getFecha_atencion());
    }

    @Override
    public int getItemCount() {
        return listaHoraUsuario.size();
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

    public  class agendaUsuariosHolder extends RecyclerView.ViewHolder{
        TextView horaAtencion, diaAtencion;
        public agendaUsuariosHolder(@NonNull View itemView) {
            super(itemView);

            diaAtencion = itemView.findViewById(R.id.txtFechaAgenda);
            horaAtencion = itemView.findViewById(R.id.txtHoraAgenda);
        }
    }
}
