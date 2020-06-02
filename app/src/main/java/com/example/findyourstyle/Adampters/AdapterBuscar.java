package com.example.findyourstyle.Adampters;

import android.content.Context;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterBuscar extends RecyclerView.Adapter<AdapterBuscar.ViewHolder> implements View.OnClickListener{

    LayoutInflater inflater;
    ArrayList<ModeloBuscar> model;
    //listener
    private View.OnClickListener listener;

    public AdapterBuscar(Context context, ArrayList<ModeloBuscar> model){
        this.inflater = LayoutInflater.from(context);
        this.model    = model;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_view_buscar, parent, false);
        //View view = LayoutInflater.from(parent.getContext().inflate(R.layout.card_view_buscar, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        //view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombreProducto.setText(model.get(position).getNombreProducto());
        holder.tienda.setText(model.get(position).getTienda());
        holder.direccion.setText(model.get(position).getDireccion());
        holder.precio.setText(model.get(position).getPrecio());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView  nombreProducto, tienda, direccion, precio;
        ImageView imgBuscar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto  = itemView.findViewById(R.id.txt_card_view_nombre_producto);
            tienda          = itemView.findViewById(R.id.txt_card_view_tienda);
            direccion       = itemView.findViewById(R.id.txt_card_view_direccion);
            precio          = itemView.findViewById(R.id.txt_card_view_precio);
            //imgBuscar       = itemView.findViewById(R.id.img_card_view_buscar);

        }
    }
}
