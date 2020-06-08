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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterBuscar extends RecyclerView.Adapter<AdapterBuscar.ViewHolder> implements View.OnClickListener{

    Context context;

    LayoutInflater inflater;
    List<ModeloBuscar> listaProductos;
    private View.OnClickListener listener;

    public AdapterBuscar(Context context, List<ModeloBuscar> listaProductos){
        this.context = context;
        this.listaProductos = listaProductos;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_buscar, viewGroup, false);
        //RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //view.setLayoutParams(layoutParams);

        //view.setOnClickListener(this);
        return new ViewHolder(v);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.nombreProducto.setText(listaProductos.get(i).getNombreProducto());
        holder.tienda.setText(listaProductos.get(i).getTienda());
        holder.direccion.setText(listaProductos.get(i).getDireccion());
        holder.precio.setText(listaProductos.get(i).getPrecio());
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
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

    public void filtrar(ArrayList<ModeloBuscar> filtroProductos) {
        this.listaProductos= filtroProductos;
        notifyDataSetChanged();
    }
}
