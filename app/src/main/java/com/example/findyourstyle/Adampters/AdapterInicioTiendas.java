package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findyourstyle.Modelo.ModelMejoresTiendas;
import com.example.findyourstyle.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterInicioTiendas extends RecyclerView.Adapter<AdapterInicioTiendas.ViewHolder> {

    LayoutInflater inflaterInicioTiendas;
    ArrayList<ModelMejoresTiendas> modelMejoresTiendas;

    public AdapterInicioTiendas(Context context, ArrayList<ModelMejoresTiendas> modelMejoresTiendas){
        this.inflaterInicioTiendas = LayoutInflater.from(context);
        this.modelMejoresTiendas   = modelMejoresTiendas;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflaterInicioTiendas.inflate(R.layout.card_view_inicio_mejores_tiendas,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nombreTienda   = modelMejoresTiendas.get(position).getNombreTienda();
        String puntos            = modelMejoresTiendas.get(position).getPuntos();
        String estrellas         = modelMejoresTiendas.get(position).getEstrellas();
        int imgMejoresTiendas = modelMejoresTiendas.get(position).getImgMejoresTiendas();

        holder.nombreTienda.setText(nombreTienda);
        holder.puntos.setText(puntos);
        holder.estrellas.setText(estrellas);
        holder.imgMejoresTiendas.setImageResource(imgMejoresTiendas);


    }

    @Override
    public int getItemCount() {
       return modelMejoresTiendas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombreTienda, puntos, estrellas;
        ImageView imgMejoresTiendas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreTienda = itemView.findViewById(R.id.txt_nt_tiendas_recomendadas);
            puntos = itemView.findViewById(R.id.txt_puntos_tiendas_recomendadas);
            estrellas = itemView.findViewById(R.id.txt_estrellas_tiendas_recomendadas);
            imgMejoresTiendas = itemView.findViewById(R.id.img_card_view_tiendas_recomendadas);
        }
    }
}
