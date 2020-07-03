package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.findyourstyle.Modelo.HorasAtencionTienda;
import com.example.findyourstyle.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterHorasTienda extends RecyclerView.Adapter<AdapterHorasTienda.AdapterHorasTiendaHolder> implements  View.OnClickListener{
     Context context;
     LayoutInflater inflater;
     List<HorasAtencionTienda> listaHorasAtencionTienda;
    RequestQueue request;

    public AdapterHorasTienda(List<HorasAtencionTienda> listaHorasAtencionTienda,Context context ) {
        this.context = context;
        this.listaHorasAtencionTienda = listaHorasAtencionTienda;
        request = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public AdapterHorasTiendaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_productos_agendados,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);

        vista.setOnClickListener(this);
        return new AdapterHorasTiendaHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHorasTiendaHolder holder, int position) {

        holder.nombreProducto.setText(listaHorasAtencionTienda.get(position).getNombreProducto());
        holder.nombreCliente.setText(listaHorasAtencionTienda.get(position).getNombreCliente());
        holder.diaAtencion.setText(listaHorasAtencionTienda.get(position).getDiaAtencion());
        holder.horaAtencion.setText(listaHorasAtencionTienda.get(position).getHoraAtencion());

        if(listaHorasAtencionTienda.get(position).getRutaImagen()!=null){

            cargarImagenServidor(listaHorasAtencionTienda.get(position).getRutaImagen(), holder);
        }else{
            holder.imgProductoAgendado.setImageResource(R.drawable.ic_launcher_background);
        }

    }
    private void cargarImagenServidor(String rutaImagen, final AdapterHorasTiendaHolder holder){
        String ip=context.getString(R.string.ip);
        String url = ip+ "/findyourstyleBDR/" + rutaImagen;
        url = url.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imgProductoAgendado.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaHorasAtencionTienda.size();
    }

    @Override
    public void onClick(View v) {

    }

    public  class AdapterHorasTiendaHolder extends RecyclerView.ViewHolder {
        ImageView imgProductoAgendado;
        TextView nombreProducto, nombreCliente,diaAtencion, horaAtencion;
        public AdapterHorasTiendaHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.txtNombrePrductosTiendaAgendados);
            nombreCliente = itemView.findViewById(R.id.txtNombreClienteAgendado);
            diaAtencion= itemView.findViewById(R.id.txtFechaAtencionAgendada);
            horaAtencion= itemView.findViewById(R.id.txtHoraAtecionAgendada);
            imgProductoAgendado= itemView.findViewById(R.id.imgProductoAgendado);

        }
    }
}
