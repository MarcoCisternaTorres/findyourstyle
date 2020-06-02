package com.example.findyourstyle.Adampters;

import android.content.Context;
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
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nombreProducto   = model.get(position).getNombreProducto();
        String tienda           = model.get(position).getTienda();
        String direccion        = model.get(position).getDireccion();
        String precio           = model.get(position).getPrecio();
        int    idImagenBuscar   = model.get(position).getIdImagenBuscar();

        holder.nombreProducto.setText(nombreProducto);
        holder.tienda.setText(tienda);
        holder.direccion.setText(direccion);
        holder.precio.setText(precio);
        holder.imgBuscar.setImageResource(idImagenBuscar);
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
            imgBuscar       = itemView.findViewById(R.id.img_card_view_buscar);

        }
    }
}
