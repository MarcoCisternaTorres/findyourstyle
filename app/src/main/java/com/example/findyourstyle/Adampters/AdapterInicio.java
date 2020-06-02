package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findyourstyle.Modelo.ModelLoMasBuscado;
import com.example.findyourstyle.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterInicio extends RecyclerView.Adapter<AdapterInicio.ViewHolder>  implements View.OnClickListener{

    LayoutInflater inflaterInicio;
    ArrayList<ModelLoMasBuscado> modelLoMasBuscados;
    private View.OnClickListener listener;

    public AdapterInicio(Context context,ArrayList<ModelLoMasBuscado> modelLoMasBuscados){
        this.inflaterInicio = LayoutInflater.from(context);
        this.modelLoMasBuscados    = modelLoMasBuscados;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflaterInicio.inflate(R.layout.card_view_inicio_lo_mas_buscado,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String nombreProducto   = modelLoMasBuscados.get(position).getNombreProducto();
        String tienda           = modelLoMasBuscados.get(position).getTienda();
        String precio           = modelLoMasBuscados.get(position).getPrecio();
        int    idImagenLoMasBuscado   = modelLoMasBuscados.get(position).getIdImagenLoMasBuscado();

        holder.nombreProducto.setText(nombreProducto);
        holder.tienda.setText(tienda);
        holder.precio.setText(precio);
        holder.imgInicioLMBr.setImageResource(idImagenLoMasBuscado);
    }

    @Override
    public int getItemCount() {
        return modelLoMasBuscados.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombreProducto, tienda, precio;
        ImageView imgInicioLMBr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto  = itemView.findViewById(R.id.txt_np_lo_mas_buscado);
            tienda          = itemView.findViewById(R.id.txt_nt_lo_mas_buscado);
            precio          = itemView.findViewById(R.id.txt_pr_lo_mas_buscado);
            imgInicioLMBr   = itemView.findViewById(R.id.img_card_view_lo_mas_buscado);

        }
    }
}
