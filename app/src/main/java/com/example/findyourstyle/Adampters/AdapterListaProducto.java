package com.example.findyourstyle.Adampters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.findyourstyle.Modelo.ModeloBuscar;
import com.example.findyourstyle.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterListaProducto extends RecyclerView.Adapter<AdapterListaProducto.productosHolder> {

    List<ModeloBuscar> listaProducto;

    public  AdapterListaProducto(List<ModeloBuscar> listaProducto){
        this.listaProducto = listaProducto;
    }
    @NonNull
    @Override
    public productosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_buscar,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new productosHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull productosHolder holder, int position) {
        holder.nombreProducto.setText(listaProducto.get(position).getNombreProducto());
        holder.nombreTienda.setText(listaProducto.get(position).getTienda());
        holder.direccion.setText(listaProducto.get(position).getDireccion());
        holder.precio.setText(listaProducto.get(position).getPrecio().toString());
    }

    @Override
    public int getItemCount() {
        return listaProducto.size();
    }

    public class productosHolder extends RecyclerView.ViewHolder {
        TextView nombreTienda, nombreProducto, direccion, precio;
        public productosHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto =  itemView.findViewById(R.id.txt_card_view_nombre_producto);
            nombreTienda =  itemView.findViewById(R.id.txt_card_view_tienda);
            direccion = itemView.findViewById(R.id.txt_card_view_direccion);
            precio =  itemView.findViewById(R.id.txt_card_view_precio);
        }
    }
}
